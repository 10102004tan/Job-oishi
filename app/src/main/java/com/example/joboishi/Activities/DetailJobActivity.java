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

import com.example.joboishi.Adapters.BenefitAdapter;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.DetailJobAPI;
import com.example.joboishi.Models.Company;
import com.example.joboishi.Models.Job;
import com.example.joboishi.Models.Jobs;
import com.example.joboishi.R;
import com.example.joboishi.databinding.DetailJobLayoutBinding;
import com.google.gson.Gson;

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

        String longDescription = "Develop, code, test, and deploy new features with a primary focus on Golang (80%) and Python (20%), ensuring adherence to API standards, extensibility, robustness, and optimal performance.\n" +
                "Maintain and enhance our high-performance architecture, creating scalable and testable components.\n" +
                "Collaborate cross-functionally with Product and Engineering teams to help define the product and system design.\n" +
                "Contribute to the development of APIs, and microservices, and potentially involved in scripting tasks.\n";

        String arr[] = longDescription.split("\n");
        //lưu khoảng cách giữa ký tự đầu dòng và nội dung
        int bulletGap = (int) dp(15);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String line = arr[i];
            SpannableString ss = new SpannableString(line);
            ss.setSpan(new BulletSpan(bulletGap), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);

            //avoid last "\n"
            if(i+1<arr.length)
                ssb.append("\n");

        }
        binding.txtResponsibilities.setText(ssb);

//        initData();
//        JobAdapter adapter = new JobAdapter(jobs);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.relatedJobs.setLayoutManager(layoutManager);
//        binding.relatedJobs.setAdapter(adapter);


        ArrayList list_benefits = new ArrayList();
        list_benefits.add("Gio lam viec linh hoat");
        list_benefits.add("Bao hien y te");
        list_benefits.add("Nghi phep");
        BenefitAdapter benefitAdapter = new BenefitAdapter(this,list_benefits);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.benefitsList.setLayoutManager(layoutManager1);

        binding.benefitsList.setAdapter(benefitAdapter);


        intent = new Intent(this, DetailCompanyActivity.class);
        binding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        jobId = "2033048";
        getDetailJob(jobId, new DetailJobCallback() {
            @Override
            public void onDetailJobLoaded(Jobs job) {
                Log.d("test", job.getId());
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

    private float dp(int dp) {
        return getResources().getDisplayMetrics().density * dp;
    }
}