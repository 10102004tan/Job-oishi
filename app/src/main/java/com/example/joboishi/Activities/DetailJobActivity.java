package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.BenefitAdapter;
import com.example.joboishi.Api.DetailJobAPI;
import com.example.joboishi.Api.JobAppliedAPI;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.Models.Jobs;
import com.example.joboishi.R;
import com.example.joboishi.databinding.DetailJobLayoutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailJobActivity extends AppCompatActivity {

    private DetailJobLayoutBinding binding;
    private ArrayList<Job> jobs;
    private Job jobDetail;
    private Intent intent;
    private String jobId;
    private DetailJobAPI detailJobAPI;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_job_layout);
        binding = DetailJobLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Init progress dialog
        progressDialog = new ProgressDialog(DetailJobActivity.this);
        progressDialog.setMessage("Please waite...");

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText("");

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Show loading indicator initially
        showLoadingIndicator();

        // Benefits recycler view
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);


        //Lay du lieu tu job api
//        jobId = "2032881";
        jobId = getIntent().getIntExtra("JOB_ID", -1) + "";
        if (jobId.equals("-1")) {
            // Không tìm thấy JOB_ID, xử lý lỗi
            Toast.makeText(this, "Job ID not found!", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu không có ID hợp lệ
            return;
        }
        getDetailJob(jobId, new DetailJobCallback() {
            @Override
            public void onDetailJobLoaded(Job job) {
                // Hide loading indicator
                hideLoadingIndicator();

//                Log.d("test", job);

                //Responsibilities
                String longDescription = job.getResponsibilities();
                SpannableStringBuilder ssb = processStringWithBullet(longDescription.trim());
                binding.txtResponsibilities.setText(ssb);

                //Requirement
                SpannableStringBuilder requirement = processStringWithBullet(job.getRequirements().trim());
                binding.txtRequirements.setText(requirement);
                //Title job
                binding.lblJobTitle.setText(job.getTitle());
                //Logo company
                Glide.with(DetailJobActivity.this)
                        .load(job.getCompany().getImage_logo())
                        .into(binding.imgCompanyLogo)
                ;
                //Company name
                binding.txtCompanyName.setText(job.getCompany().getDisplay_name());

                //Job Salary
                if(!job.getIs_salary_visible()) {
                    binding.txtSalary.setText("Công ty bảo mật thông tin này");
                }
                else {
                    binding.txtSalary.setText(job.getSalary_value());
                }

                //Company location
                binding.txtLocation.setText(job.getCompany().getAddress().get(0).getDistrict() + ", " + job.getCompany().getAddress().get(0).getProvince());
                //Job content
                binding.txtJobContent.setText(job.getContent());


                //Skills
                // Xóa tất cả các TextView hiện có từ LinearLayout
                binding.skillsLayout.removeAllViews();

                // Thêm các TextView mới
                for (String item : job.getSkills()) {
                    TextView textView = new TextView(DetailJobActivity.this);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    textView.setText(item);
                    textView.setBackgroundResource(R.drawable.text_badge);
                    textView.setPadding(15, 5, 15, 5); // Thêm padding nếu cần
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    params.setMargins(0, 0, 20, 0); // Khoảng cách giữa các TextView
                    textView.setLayoutParams(params);
                    binding.skillsLayout.addView(textView); // Thêm TextView vào LinearLayout
                }


                //Benefits
                if(job.getBenefit() != null && job.getBenefit().size() > 0) {
                    binding.getBenefits.setVisibility(View.VISIBLE);
                    binding.getBenefitsTitle.setVisibility(View.VISIBLE);
                BenefitAdapter benefitAdapter = new BenefitAdapter(DetailJobActivity.this,job.getBenefit());
                binding.benefitsList.setLayoutManager(layoutManager1);
                binding.benefitsList.setAdapter(benefitAdapter);
                }
                else {
                    binding.getBenefits.setVisibility(View.GONE);
                    binding.getBenefitsTitle.setVisibility(View.GONE);
                }

                //Get Company logo
                binding.companyName.setText(job.getCompany().getDisplay_name());
                binding.txtCompanySize.setText(job.getCompany().getCompany_size());
                Glide.with(DetailJobActivity.this)
                        .load(job.getCompany().getImage_logo())
                        .into(binding.avatarCompany)
                ;

                //Get Detail Company's Job
                //Bat su kiem chuyen sanng chi tiet cong ty
                intent = new Intent(DetailJobActivity.this, DetailCompanyActivity.class);
                intent.putExtra("COMPANY_ID", job.getCompany().getId());
                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("test", job.getCompany().getId() + "");
                        startActivity(intent);
                    }
                });

                binding.txtCompanyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(intent);
                    }
                });

                binding.imgCompanyLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                });

                //Applied Job Event From Serve
                if (job.isIs_edit()) {
                    binding.btnApplied.setEnabled(true);
                }
                else {
                    binding.btnApplied.setEnabled(false);
                }
                binding.btnApplied.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Lấy ngày giờ hiện tại
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

                        String formattedDateTime = currentDateTime.format(formatter);
                        Log.d("test", formattedDateTime);
                        String address = job.getCompany().getAddress().get(0).getDistrict() + ", " +  job.getCompany().getAddress().get(0).getDistrict() + ", " + job.getCompany().getAddress().get(0).getProvince();
                        AppliedJob(
                                job.getId() + "",
                                "1",
                                job.getTitle(),
                                job.getCompany().getId() + "",
                                job.getCompany().getImage_logo(),
                                address,
                                true ,
                                job.getIs_salary_visible(),
                                formattedDateTime
                                );
                    }
                });

            }

            @Override
            public void onDetailJobFailed(String errorMessage) {
                // Xử lý khi có lỗi xảy ra
            }
        });

    }


    //Ham gọi API
    private void getDetailJob(String jobId, DetailJobCallback callback) {
        //Tao doi tuong retrofit
//        Log.d("test", DetailJobAPI.BASE_URL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DetailJobAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        detailJobAPI = retrofit.create(DetailJobAPI.class);
        Call<Job> call = detailJobAPI.getJobDetail(jobId);
        call.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if(response.isSuccessful()){
                    Job detailJob = response.body();
                    assert detailJob != null;
                    callback.onDetailJobLoaded(detailJob);
                } else {
                    callback.onDetailJobFailed("Failed to get job details");
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                callback.onDetailJobFailed("Failed to get job details: " + t.getMessage());
            }
        });
    }

    public interface DetailJobCallback {
        void onDetailJobLoaded(Job job);
        void onDetailJobFailed(String errorMessage);
    }

    //Ham Applied Job
    private void AppliedJob (String id, String user_id, String title ,String company_id, String company_logo, String sort_addresses,Boolean is_applied, Boolean is_salary_visible, String published) {
        //Tao Retrofit
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JobAppliedAPI.BASE_URL)
                .build();
        JobAppliedAPI jobAppliedAPI = retrofit.create(JobAppliedAPI.class);

        //Chuyển đổi các tham số thành RequestBody
        RequestBody idRequestBody = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody user_idRequestBody = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody titleRequestBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody company_idRequestBody = RequestBody.create(MediaType.parse("text/plain"), company_id);
        RequestBody company_logoRequestBody = RequestBody.create(MediaType.parse("text/plain"), company_logo);
        RequestBody sort_addressesRequestBody = RequestBody.create(MediaType.parse("text/plain"), sort_addresses);
        RequestBody is_appliedRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(is_applied));
        RequestBody is_salary_visibleRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(is_salary_visible));
        RequestBody publishedRequestBody = RequestBody.create(MediaType.parse("text/plain"), published);

        //Gửi yêu cầu tải lên
        Call<ResponseBody> call = jobAppliedAPI.applied(
                idRequestBody, user_idRequestBody, titleRequestBody, company_idRequestBody, company_logoRequestBody, sort_addressesRequestBody, is_appliedRequestBody ,is_salary_visibleRequestBody, publishedRequestBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
//                    Log.d("test", "Response");
                    progressDialog.dismiss();
                    showDialog();
                }
                else {
                    progressDialog.dismiss();
                    Log.d("test", "Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    //Ham xu ly chuoi thanh cac dau cham dau dong
    private SpannableStringBuilder processStringWithBullet(String longDescription){
        String arr[] = longDescription.split("\n");
        //lưu khoảng cách giữa ký tự đầu dòng và nội dung
        int bulletGap = (int) getResources().getDisplayMetrics().density * 15;

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String line = arr[i];
            SpannableString ss = new SpannableString(line);
            ss.setSpan(new BulletSpan(bulletGap), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);

            // Tránh thêm "\n" cuối cùng
            if (i + 1 < arr.length)
                ssb.append("\n");

            // Thêm khoảng trống sau mỗi đoạn văn
            ssb.append("\n");

        }
        return ssb;
    }

    //Ham hien thi loading
    private void showLoadingIndicator() {
        // Show loading indicator (e.g., ProgressBar)
        binding.layoutContent.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    //Ham an loading
    private void hideLoadingIndicator() {
        // Hide loading indicator
        binding.layoutContent.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showDialog() {
        //Create the Dialog here
        Dialog dialog = new Dialog(DetailJobActivity.this);
        dialog.setContentView(R.layout.custom_dialog_layout);
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