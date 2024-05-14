package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.BenefitAdapter;
import com.example.joboishi.Api.DetailJobAPI;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.Models.Jobs;
import com.example.joboishi.R;
import com.example.joboishi.databinding.DetailJobLayoutBinding;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_job_layout);
        binding = DetailJobLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Show loading indicator initially
        showLoadingIndicator();

        // Lợi ích recycler view
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);



        //Bat su kiem chuyen sanng chi tiet cong ty
        intent = new Intent(this, DetailCompanyActivity.class);
        binding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        //Lay du lieu tu job api
        jobId = "2032881";
        getDetailJob(jobId, new DetailJobCallback() {
            @Override
            public void onDetailJobLoaded(Job job) {
                // Hide loading indicator
                hideLoadingIndicator();

                Log.d("test", job.getContent());

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
                if(job.getBenefit() != null) {
                BenefitAdapter benefitAdapter = new BenefitAdapter(DetailJobActivity.this,job.getBenefit());
                binding.benefitsList.setLayoutManager(layoutManager1);
                binding.benefitsList.setAdapter(benefitAdapter);
                }

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
        Log.d("test", DetailJobAPI.BASE_URL);
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
}