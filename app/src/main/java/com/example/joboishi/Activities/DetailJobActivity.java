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
    private Jobs jobDetail = new Jobs();
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

//        initData();
//        JobAdapter adapter = new JobAdapter(jobs);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.relatedJobs.setLayoutManager(layoutManager);
//        binding.relatedJobs.setAdapter(adapter);

        // Lợi ích recycler view

        ArrayList list_benefits = new ArrayList();
        list_benefits.add("Gio lam viec linh hoat");
        list_benefits.add("Bao hien y te");
        list_benefits.add("Nghi phep");
        BenefitAdapter benefitAdapter = new BenefitAdapter(this,list_benefits);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.benefitsList.setLayoutManager(layoutManager1);
        binding.benefitsList.setAdapter(benefitAdapter);

        //Bat su kiem chuyen sanng chi tiet cong ty
        intent = new Intent(this, DetailCompanyActivity.class);
        binding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        //Lay du lieu tu job api
        jobId = "2032880";
        getDetailJob(jobId, new DetailJobCallback() {
            @Override
            public void onDetailJobLoaded(Jobs job) {
                // Hide loading indicator
                hideLoadingIndicator();

                String longDescription = job.getResponsibilities();
                SpannableStringBuilder ssb = processStringWithBullet(longDescription.trim());
                binding.txtResponsibilities.setText(ssb);

                SpannableStringBuilder requirement = processStringWithBullet(job.getRequirements().trim());
                binding.txtRequirements.setText(requirement);

                binding.lblJobTitle.setText(job.getTitle());
                Glide.with(DetailJobActivity.this)
                        .load(job.getCompany_logo())
                        .into(binding.imgCompanyLogo)
                ;

                binding.txtCompanyName.setText(job.getCompany_name());
                binding.txtJobContent.setText(job.getContent());


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

            }

            @Override
            public void onDetailJobFailed(String errorMessage) {
                // Xử lý khi có lỗi xảy ra
            }
        });

    }

//    private void initData(){
//        this.jobs = new ArrayList<>();
//        Job job = new Job("Back-end Developer", "Google", "California", new Company("FPT Software", "California", "google.com", "Quan 3, Thanh Pho Ho Chi Minh"));
//        this.jobs.add(job);
//        this.jobs.add(job);
//        this.jobs.add(job);
//        this.jobs.add(job);
//        this.jobs.add(job);
//
//    }

    //Ham gọi API
    private void getDetailJob(String jobId, DetailJobCallback callback) {
        //Tao doi tuong retrofit
        Log.d("test", DetailJobAPI.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DetailJobAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        detailJobAPI = retrofit.create(DetailJobAPI.class);
        Call<Jobs> call = detailJobAPI.getJobDetail(jobId);
        call.enqueue(new Callback<Jobs>() {
            @Override
            public void onResponse(Call<Jobs> call, Response<Jobs> response) {
                if(response.isSuccessful()){
                    Jobs detailJobs = response.body();
                    assert detailJobs != null;
                    callback.onDetailJobLoaded(detailJobs);
                } else {
                    callback.onDetailJobFailed("Failed to get job details");
                }
            }

            @Override
            public void onFailure(Call<Jobs> call, Throwable t) {
                callback.onDetailJobFailed("Failed to get job details: " + t.getMessage());
            }
        });
    }

    public interface DetailJobCallback {
        void onDetailJobLoaded(Jobs job);
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