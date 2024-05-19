package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.BaseFragment;
import com.example.joboishi.databinding.FragmentAppliedJobBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class AppliedJobFragment extends BaseFragment {

    private FragmentAppliedJobBinding binding;
    private ArrayList<JobBasic> jobs;
    private IJobsService iJobsService;
    private  JobAdapter adapter;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirstLoading = true;
    private int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppliedJobBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", 1);
        jobs = new ArrayList<>();
        adapter = new JobAdapter(jobs,getContext());
        adapter.setVisibleBookmark(false);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.listJob.setAdapter(adapter);
        getJobsApplied();

        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(int id) {
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("JOB_ID",id);
                Log.d("JOB_ID", String.valueOf(id));
                startActivity(intent);
            }

            @Override
            public void onAddJobBookmark(JobBasic job, ImageView bookmarkImage, int position) {

            }

            @Override
            public void onRemoveBookmark(JobBasic jobBasic, ImageView bookmarkImage, int position) {

            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstLoading = true;
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                if (statusPreInternet == STATUS_NO_INTERNET){
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.listJob.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                }
                else{
                    getJobsApplied();
                    binding.listJob.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }


    /*
    CREATE METHODS FOR INTERNET BROADCAST RECEIVER
    */
    private void getJobsApplied(){
        if (isFirstLoading){
            binding.image.setVisibility(View.VISIBLE);
            binding.image.setAnimation(R.raw.fetch_api_loading);
            binding.image.playAnimation();
            isFirstLoading = false;
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        Call<ArrayList<JobBasic>> call = iJobsService.getJobApplied(userId);
        call.enqueue(new Callback<ArrayList<JobBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                if (response.isSuccessful()){
                    jobs = response.body();
                    binding.swipeRefreshLayout.setRefreshing(false);
                    if(jobs.size() == 0){
                        binding.image.setAnimation(R.raw.no_data);
                        binding.image.playAnimation();
                        binding.listJob.setVisibility(View.GONE);
                    }
                    else{
                        binding.listJob.setVisibility(View.VISIBLE);
                        binding.image.setVisibility(View.GONE);
                    }
                    adapter.updateData(jobs);

                    Gson gson = new Gson();
                    String json = gson.toJson( response.body());
                    Log.d("JOBS",json);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void handleNoInternet() {
        statusPreInternet = STATUS_NO_INTERNET;
        if (isFirst) {
            binding.listJob.setVisibility(View.GONE);
            binding.image.setVisibility(View.VISIBLE);
            binding.image.setAnimation(R.raw.a404);
            binding.image.playAnimation();
            statusInternet = STATUS_NO_INTERNET;
            binding.swipeRefreshLayout.setRefreshing(false);
            isFirst = false;
            ;
        }

        MotionToast.Companion.createToast(getActivity(), "😍",
                "Không có kết nối mạng",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
    }

    @Override
    protected void handleLowInternet() {
        MotionToast.Companion.createToast(getActivity(), "😍",
                "Đang kết nối ...",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
    }

    @Override
    protected void handleGoodInternet() {
        statusPreInternet = STATUS_GOOD_INTERNET;
        if (isFirst) {
            statusInternet = STATUS_GOOD_INTERNET;
            isFirst = false;
        }
        else{
            if (statusInternet == STATUS_NO_INTERNET){
                binding.image.setVisibility(View.GONE);
                binding.listJob.setVisibility(View.VISIBLE);
            }

        }
    }
}