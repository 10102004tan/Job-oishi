package com.example.joboishi.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joboishi.Api.UploadAPI;
import com.example.joboishi.Models.RealPathUtil;
import com.example.joboishi.Models.data.FileCV;
import com.example.joboishi.R;
import com.example.joboishi.databinding.UploadFileLayoutBinding;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadFileActivity extends AppCompatActivity {

    private UploadFileLayoutBinding binding;
    private final int REQ = 999;
    private Uri mUri;
    private ProgressDialog mProgressDialog;

    private ActivityResultLauncher<Intent> launcher;
    private FileContent file = new FileContent();
    private String userID = "111";
    private long LIMIT_SIZE = 500000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_file_layout);
        binding = UploadFileLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Init progress dialog
        mProgressDialog = new ProgressDialog(UploadFileActivity.this);
        mProgressDialog.setMessage("Please waite...");

        //Shimmer
        binding.shimmerUpload.startShimmerAnimation();
        binding.shimmerUpload.setVisibility(View.VISIBLE);
        binding.btnUpload.setVisibility(View.INVISIBLE);

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText("Upload CV");

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = now.format(formatter);

        //Lấy cv của người dùng (nếu có)
        callAPIGetFile(userID);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null) {
                                return;
                            }
                            //Get PDF URi
                            Uri uri = data.getData();
                            mUri = uri;

                            // Get the file name from the URI
                            file = getFileContentFromUri(uri);

                            //Set infor


                            if (!file.typeFile.equalsIgnoreCase("pdf")) {
                                showDialog("File được chọn không đúng định dạng!", false);
                                binding.btnPush.setEnabled(false);
//                                Log.d("test", file.typeFile);

                            } else {
                                if(file.getFileSize() <= LIMIT_SIZE) {
                                    binding.btnPush.setEnabled(true);
                                    String fileInfor = formatFileSize(file.getFileSize()) + "• Upload " + formattedDateTime;
                                    binding.lblFileName.setText(file.fileName);
                                    binding.lblFileInfo.setText(fileInfor);
                                    binding.btnUpload.setVisibility(View.GONE);
                                    binding.layoutFileInfo.setVisibility(View.VISIBLE);
                                }
                                else {
                                    showDialog("File được chọn Vượt quá 5MB!", false);
                                    binding.btnPush.setEnabled(false);
                                }
                            }

                        }
                    }
                }
        );

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequirementPermission();
//                Log.d("test", file.toString());
            }
        });

        binding.btnDeleteCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPIDeleteFile(userID);
            }
        });

        binding.btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPIUploadFile(userID, file.getFileName(), formatFileSize(file.getFileSize()), formattedDateTime);
//                Log.d("test",file.toString());
                Log.d("test", "is upload");

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void onClickRequirementPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openFile();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, REQ);
            openFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode >= REQ && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Khi duoc cho phep thi goi ham openFile
            openFile();
        }
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        launcher.launch(intent);
    }

    private void callAPIUploadFile(String user_id, String file_name, String file_size, String upload_at) {
        mProgressDialog.show();
        if (mUri != null) {
            try {
                //  mở một luồng đầu vào (input stream) để đọc dữ liệu từ tệp được chỉ định bởi URI mUri
                InputStream inputStream = getContentResolver().openInputStream(mUri);

                // Tạo một bộ đệm để đọc dữ liệu từ InputStream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                // Chuyển dữ liệu từ ByteArrayOutputStream thành một mảng byte
                byte[] bytes = outputStream.toByteArray();

                // Tạo request body multipart
                RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("pdf", "file_name", requestBodyFile);

                // Tạo request body
                RequestBody requestBodyUserId = RequestBody.create(MediaType.parse("text/plain"), user_id);
                RequestBody requestBodyFileName = RequestBody.create(MediaType.parse("text/plain"), file_name);
                RequestBody requestBodyFileSize = RequestBody.create(MediaType.parse("text/plain"), file_size);
                RequestBody requestBodyUploadAt = RequestBody.create(MediaType.parse("text/plain"), upload_at);

                // Tạo Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UploadAPI.BASE_URL)
                        .build();

                // Tạo instance của ApiService từ Retrofit
                UploadAPI uploadAPI = retrofit.create(UploadAPI.class);

                // Gửi yêu cầu tải lên
                Call<ResponseBody> call = uploadAPI.uploadFile(filePart, requestBodyUserId, requestBodyFileName, requestBodyFileSize, requestBodyUploadAt);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            mProgressDialog.dismiss();
                            binding.btnUpload.setVisibility(View.GONE);
                            binding.btnPush.setEnabled(false);

                            Log.d("test", "Response");
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String message = jsonObject.getString("name");
                                showDialog(message, true);
                                Log.d("test", "File api: " + message);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mProgressDialog.dismiss();
                            Log.d("test", "Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mProgressDialog.dismiss();
                        Log.e("test", "Upload failed: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mProgressDialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
                mProgressDialog.dismiss();
            }
        }
    }

    //Ham get file theo user_id
    public void callAPIGetFile(String user_id) {
        Log.d("test", user_id);
        Log.d("test", UploadAPI.BASE_URL);
        //Tạo retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UploadAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Tạo instance của ApiService từ Retrofit
        UploadAPI uploadAPI = retrofit.create(UploadAPI.class);
        // Gửi yêu cầu tải lên
        Call<FileCV> call = uploadAPI.getFile(user_id);
        call.enqueue(new Callback<FileCV>() {
            @Override
            public void onResponse(Call<FileCV> call, Response<FileCV> response) {
                if(response.isSuccessful()) {
                    FileCV fileCV = response.body();
                    //Shimmer
                    binding.shimmerUpload.stopShimmerAnimation();
                    binding.shimmerUpload.setVisibility(View.GONE);
                    binding.layoutFileInfo.setVisibility(View.VISIBLE);

                    binding.lblFileName.setText(fileCV.getFileName());
                    String fileInfor = fileCV.getFileSize() + " • Upload " + fileCV.getUploadAt();
                    binding.lblFileInfo.setText(fileInfor);
                    binding.btnUpload.setVisibility(View.GONE);
                    Log.d("test", "Lấy dữ liệu thành công");

                }
                else {
                    //Shimmer
                    binding.shimmerUpload.stopShimmerAnimation();
                    binding.shimmerUpload.setVisibility(View.GONE);
                    binding.btnUpload.setVisibility(View.VISIBLE);
                    Log.d("test", "Lấy thông tin thất bại" + response.message());
                }
            }

            @Override
            public void onFailure(Call<FileCV> call, Throwable t) {
                //Shimmer
                binding.shimmerUpload.stopShimmerAnimation();
                binding.shimmerUpload.setVisibility(View.GONE);
//                binding.btnUpload.setVisibility(View.VISIBLE);
                Log.d("test", "Failure Lấy thông tin thất bại");
                t.printStackTrace();
            }
        });
    }

//  //Ham delete file
    public void callAPIDeleteFile(String user_id){
        mProgressDialog.show();
        //Tạo retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UploadAPI.BASE_URL)
                .build();
        // Tạo instance của ApiService từ Retrofit
        UploadAPI uploadAPI = retrofit.create(UploadAPI.class);
        // Gửi yêu cầu tải lên
        Call<ResponseBody> call = uploadAPI.deleteFile(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    mProgressDialog.dismiss();
                    binding.layoutFileInfo.setVisibility(View.GONE);
                    binding.btnUpload.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String message = jsonObject.getString("message");
                        showDialog(message, true);
//                        Log.d("test", "File api: " + message);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("test", "Xóa tệp thất bại: " + response.message());
                    mProgressDialog.dismiss();
                    binding.layoutFileInfo.setVisibility(View.GONE);
                    binding.btnUpload.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProgressDialog.dismiss();
                binding.layoutFileInfo.setVisibility(View.GONE);
                binding.btnUpload.setVisibility(View.VISIBLE);
                Log.d("test", "Failure Xóa tệp thất bại");
            }
        });
    }

    // Hàm lấy thông tin từ file uri
    private FileContent getFileContentFromUri(Uri uri) {
        String fileName = "";
        String fileExtension = "";
        long fileSize = 0;
        FileContent fileContent = new FileContent();

        // Lấy tên tệp từ Uri
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(OpenableColumns.SIZE));
                }
            }
        }

        // Lấy định dạng tệp
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(dotIndex + 1);
        }

        fileContent.setFileName(fileName);
        fileContent.setTypeFile(fileExtension);
        fileContent.setFileSize(fileSize);

        return fileContent;
    }

    //Show dialog
    private void showDialog(String message, Boolean type) {
        //Create the Dialog here
        Dialog dialog = new Dialog(UploadFileActivity.this);
        dialog.setContentView(R.layout.custom_dialog_layout);

        TextView lbl_title = dialog.findViewById(R.id.textView2);
        lbl_title.setText(message);
        if (type) {
            ImageView imageView = dialog.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_checked);
            TextView lbl_Name = dialog.findViewById(R.id.textView);
            lbl_Name.setText("Successful");
        } else {
            ImageView imageView = dialog.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_failed);
            TextView lbl_Name = dialog.findViewById(R.id.textView);
            lbl_Name.setText("Failed");
        }


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        dialog.show();

        Button Okay = dialog.findViewById(R.id.btn_okay);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class FileContent {
        private String fileName;
        private String typeFile;
        private long fileSize;

        public FileContent() {
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getTypeFile() {
            return typeFile;
        }

        public void setTypeFile(String typeFile) {
            this.typeFile = typeFile;
        }

        @Override
        public String toString() {
            return "FileContent{" +
                    "fileName='" + fileName + '\'' +
                    ", typeFile='" + typeFile + '\'' +
                    ", fileSize=" + fileSize +
                    '}';
        }
    }

    //Format file size
    private String formatFileSize(long size){
        final long KB = 1024;
        final long MB = KB * KB;
        final long GB = MB * MB;

        if(size < KB) {
            return size + " bytes";
        }
        else if (size < MB) {
            return String.format("%.2f KB", (float) size / KB);
        }
        else if (size < MB) {
            return String.format("%.2f MB", (float) size / MB);
        }
        else {
            return String.format("%.2f GB", (float) size / GB);
        }
    }
}