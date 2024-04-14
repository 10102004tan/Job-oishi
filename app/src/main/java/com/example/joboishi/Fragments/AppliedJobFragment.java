package com.example.joboishi.Fragments;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Models.Company;
import com.example.joboishi.Models.Job;
import com.example.joboishi.R;
import com.example.joboishi.databinding.FragmentAppliedJobBinding;
import com.example.joboishi.databinding.FragmentMyJobBinding;
import com.example.joboishi.databinding.FragmentSavedJobBinding;

import java.util.ArrayList;

public class AppliedJobFragment extends Fragment {

   private FragmentAppliedJobBinding binding;
    private ArrayList<Job> jobs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppliedJobBinding.inflate(inflater, container, false);
        initData();

        if (jobs.size() != 0) {
            binding.listJob.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);
            JobAdapter adapter = new JobAdapter(jobs);
            binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            binding.listJob.setAdapter(adapter);
        }
        else{
            binding.noData.setVisibility(View.VISIBLE);
            binding.listJob.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }
    private void initData() {
        this.jobs = new ArrayList<>();
        Job job = new Job("Front-end Developer", "Google", "California", new Company("FPT Software", "California", "google.com", "Quan 3, Thanh Pho Ho Chi Minh"));
//        this.jobs.add(job);
//        this.jobs.add(job);
//        this.jobs.add(job);
//        this.jobs.add(job);
//        this.jobs.add(job);

    }
}