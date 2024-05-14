package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Models.Company;
import com.example.joboishi.Models.Job;
import com.example.joboishi.Models.JobBasic;
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
    private ArrayList<JobBasic> jobList;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobBinding.inflate(inflater, container, false);
        jobList = new ArrayList<>();
        binding.loading.setVisibility(View.VISIBLE);
        binding.loading.startShimmerAnimation();
        adapter = new JobAdapter(jobList,getContext());
        adapter.setBookmark(true);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.listJob.setAdapter(adapter);

        //Add event for adapter
       adapter.setiClickJob(new JobAdapter.IClickJob() {
           @Override
           public void onClickJob(int id) {

           }

           @Override
           public void onClickBookmark(JobBasic job) {
               jobList.remove(job);
               adapter.updateData(jobList);

               //processing post id job remove bookmark

           }
       });

        getJobsSaved();
        //Add event for swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                getJobsSaved();

            }
        });
        return binding.getRoot();
    }

    private void getJobsSaved(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        Call<ArrayList<JobBasic>> call = iJobsService.getAllJobsBookmarkById(0);
        call.enqueue(new Callback<ArrayList<JobBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                if (response.isSuccessful()){
                    jobList = response.body();
                    binding.loading.setVisibility(View.GONE);
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.loading.stopShimmerAnimation();
                    adapter.updateData(jobList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                Log.d("testttttt",t.getMessage());
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
                    binding.imageNoInternet.setVisibility(View.VISIBLE);
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
    /*
    HANDLE NO INTERNET CONNECTION
    */
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