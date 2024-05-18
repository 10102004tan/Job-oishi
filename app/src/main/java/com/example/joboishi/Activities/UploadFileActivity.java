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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.joboishi.Api.UploadAPI;
import com.example.joboishi.Models.RealPathUtil;
import com.example.joboishi.R;
import com.example.joboishi.databinding.UploadFileLayoutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadFileActivity extends AppCompatActivity {

    private UploadFileLayoutBinding binding;
    private final int REQ =  999;
    private Uri mUri;
    private ProgressDialog mProgressDialog;

    private ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_file_layout);
        binding = UploadFileLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Init progress dialog
        mProgressDialog = new ProgressDialog(UploadFileActivity.this);
        mProgressDialog.setMessage("Please waite...");

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            if (data == null) {
                                return;
                            }
                            //Get PDF URi
                            Uri uri = data.getData();
                            mUri = uri;

                            // Get the file name from the URI
                            String fileName = getFileNameFromUri(uri);
                            binding.lblFileName.setText(fileName);
                        }
                    }
                }
        );

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequirementPermission();
            }
        });

        binding.btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPIUploadFile();
                Log.d("test", "is upload");
            }
        });

    }

    private void onClickRequirementPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openFile();
        }
        else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode >= REQ  && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    private void callAPIUploadFile() {
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

                // Tạo request body cho user_id
                RequestBody requestBodyUserId = RequestBody.create(MediaType.parse("multipart/form-data"), "7");

                // Tạo Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UploadAPI.BASE_URL)
                        .build();

                // Tạo instance của ApiService từ Retrofit
                UploadAPI uploadAPI = retrofit.create(UploadAPI.class);

                // Gửi yêu cầu tải lên
                Call<ResponseBody> call = uploadAPI.uploadFile(filePart, requestBodyUserId);
                // Tiếp tục xử lý phản hồi như bạn đã làm trước đó
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            mProgressDialog.dismiss();

                            Log.d("test", "Response");
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String message = jsonObject.getString("message");
                                showDialog(message);
//                                Log.d("test", "File: " + message);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
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

    // Method to get the file name from the URI
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void showDialog(String message) {
        //Create the Dialog here
        Dialog dialog = new Dialog(UploadFileActivity.this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        TextView lbl_title = dialog.findViewById(R.id.textView2);
        lbl_title.setText(message);
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
}