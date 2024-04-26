package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.View;

import com.example.joboishi.Adapters.BenefitAdapter;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Models.Company;
import com.example.joboishi.Models.Job;
import com.example.joboishi.R;
import com.example.joboishi.databinding.DetailJobLayoutBinding;

import java.util.ArrayList;

public class DetailJobActivity extends AppCompatActivity {

    private DetailJobLayoutBinding binding;
    private ArrayList<Job> jobs;
    private Intent intent;
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

        initData();
        JobAdapter adapter = new JobAdapter(jobs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.relatedJobs.setLayoutManager(layoutManager);
        binding.relatedJobs.setAdapter(adapter);


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
    }

    private void initData(){
        this.jobs = new ArrayList<>();
        Job job = new Job("Back-end Developer", "Google", "California", new Company("FPT Software", "California", "google.com", "Quan 3, Thanh Pho Ho Chi Minh"));
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);

    }

    private float dp(int dp) {
        return getResources().getDisplayMetrics().density * dp;
    }
}