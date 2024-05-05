package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Models.Company;
import com.example.joboishi.Models.Job;
import com.example.joboishi.databinding.FragmentSavedJobBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SavedJobFragment extends Fragment {

    private FragmentSavedJobBinding binding;
    private ArrayList<Job> jobList;
    private JobAdapter adapter;
    private InternetBroadcastReceiver internetBroadcastReceiver;
    private IntentFilter intentFilter;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;

    private IJobsService iJobsService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobBinding.inflate(inflater, container, false);
        jobList = new ArrayList<>();
        binding.loading.setVisibility(View.VISIBLE);
        binding.loading.startShimmerAnimation();


        //
        adapter = new JobAdapter(jobList,getContext());

        /*Add event for job item*/


        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.listJob.setAdapter(adapter);
        getJobsSaved();





        //Add event for swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }



    private void getJobsSaved(){
        jobList.clear();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        Call<ArrayList<Job>> call = iJobsService.getListJobs();
        call.enqueue(new Callback<ArrayList<Job>>() {
            @Override
            public void onResponse(Call<ArrayList<Job>> call, Response<ArrayList<Job>> response) {
                if (response.isSuccessful()){
                    jobList = response.body();
                    binding.loading.setVisibility(View.GONE);
                    binding.loading.stopShimmerAnimation();}
            }
            @Override
            public void onFailure(Call<ArrayList<Job>> call, Throwable t) {
                Log.d("error",t.getMessage()+"");
            }
        });
    }

    /*
    CREATE METHODS FOR INTERNET BROADCAST RECEIVER
    */

    private void registerInternetBroadcastReceiver() {
        internetBroadcastReceiver = new InternetBroadcastReceiver();
        internetBroadcastReceiver.listener = new InternetBroadcastReceiver.IInternetBroadcastReceiverListener() {
            @Override
            public void noInternet() {
                statusPreInternet = STATUS_NO_INTERNET;
                if (isFirst) {
                    binding.listJob.setVisibility(View.GONE);
                    binding.imageNoData.setVisibility(View.GONE);
                    binding.loading.setVisibility(View.VISIBLE);
                    binding.imageNoInternet.setVisibility(View.GONE);
                    binding.loading.startShimmerAnimation();
                    handlerNoInternet();
                    statusInternet = STATUS_NO_INTERNET;
                    isFirst = false;
                }
            }

            @Override
            public void lowInternet() {
            }

            @Override
            public void goodInternet() {
                statusPreInternet = STATUS_GOOD_INTERNET;
                if (isFirst) {
                    binding.listJob.setVisibility(View.VISIBLE);
                    binding.imageNoInternet.setVisibility(View.GONE);
                    statusInternet = STATUS_GOOD_INTERNET;
                    getJobsSaved();
                    isFirst = false;
                }
            }
        };
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    /*
    HANDLE NO INTERNET CONNECTION
    */
    private void handlerNoInternet(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.noData.setVisibility(View.VISIBLE);
                binding.imageNoInternet.setVisibility(View.VISIBLE);
                binding.loading.setVisibility(View.GONE);
                binding.loading.stopShimmerAnimation();
            }
        }, 2000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (internetBroadcastReceiver != null) {
            getActivity().unregisterReceiver(internetBroadcastReceiver);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        registerInternetBroadcastReceiver();
    }
}