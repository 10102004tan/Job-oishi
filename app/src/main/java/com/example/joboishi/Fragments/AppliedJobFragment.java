package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Fragments.BottomSheetDialog.FilterJobFragment;
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
    private InternetBroadcastReceiver internetBroadcastReceiver;
    private IntentFilter intentFilter;
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

        //Processing bottom sheet dialog filter
        binding.btnFiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();
               dialogFragment.setFragment(new FilterJobFragment());
               dialogFragment.show(getActivity().getSupportFragmentManager(), dialogFragment.getTag());
            }
        });

        return binding.getRoot();
    }
    private void initData() {
        this.jobs = new ArrayList<>();
        Job job = new Job("Front-end Developer", "Google", "California", new Company("FPT Software", "California", "google.com", "Quan 3, Thanh Pho Ho Chi Minh"));
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);
        this.jobs.add(job);

    }


    /*
    CREATE METHODS FOR INTERNET BROADCAST RECEIVER
    */

    private void registerInternetBroadcastReceiver() {
        internetBroadcastReceiver = new InternetBroadcastReceiver();
        internetBroadcastReceiver.listener = new InternetBroadcastReceiver.IInternetBroadcastReceiverListener() {
            @Override
            public void noInternet() {
                binding.listJob.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                binding.imageNoInternet.setVisibility(View.VISIBLE);

                binding.listJob.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                binding.loading.startShimmerAnimation();
                run();
            }

            @Override
            public void lowInternet() {
                binding.listJob.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
                binding.loading.setVisibility(View.VISIBLE);
                binding.loading.startShimmerAnimation();
            }

            @Override
            public void goodInternet() {
                binding.listJob.setVisibility(View.VISIBLE);
                binding.noData.setVisibility(View.GONE);
                binding.loading.setVisibility(View.GONE);
                binding.loading.stopShimmerAnimation();
            }
        };

        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }
    @Override
    public void onResume() {
        super.onResume();
        registerInternetBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void run(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.loading.setVisibility(View.INVISIBLE);
                binding.noData.setVisibility(View.VISIBLE);
                binding.loading.stopShimmerAnimation();
            }
        }, 3000);
    }
}