package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Models.Company;
import com.example.joboishi.Models.Job;
import com.example.joboishi.R;
import com.example.joboishi.databinding.CompanyLayoutBinding;
import com.example.joboishi.databinding.DetailJobLayoutBinding;

import java.util.ArrayList;

public class DetailCompanyActivity extends AppCompatActivity {

    private CompanyLayoutBinding binding;
    private ArrayList<Job> jobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_layout);
        binding = CompanyLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        initData();
//        JobAdapter adapter = new JobAdapter(jobs);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.jobsOfCompany.setLayoutManager(layoutManager);
//
//        binding.jobsOfCompany.setAdapter(adapter);
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
}