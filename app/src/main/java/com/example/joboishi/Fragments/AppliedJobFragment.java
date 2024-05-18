package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Fragments.BottomSheetDialog.FilterJobFragment;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.BaseFragment;
import com.example.joboishi.databinding.FragmentAppliedJobBinding;
import com.example.joboishi.databinding.FragmentMyJobBinding;
import com.example.joboishi.databinding.FragmentSavedJobBinding;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

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
    FilterJobBSDFragment filterJobBottomSheet;
    private IJobsService iJobsService;
    private  JobAdapter adapter;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirstLoading = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppliedJobBinding.inflate(inflater, container, false);
        jobs = new ArrayList<>();
        filterJobBottomSheet = FilterJobBSDFragment.newInstance();

        adapter = new JobAdapter(jobs,getContext());
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.listJob.setAdapter(adapter);

        //Processing bottom sheet dialog filter
        binding.btnFiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterJobBottomSheet.show(getActivity().getSupportFragmentManager(), filterJobBottomSheet.getTag());
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
                getJobsApplied();
                if (statusInternet == STATUS_NO_INTERNET){
                    binding.swipeRefreshLayout.setRefreshing(false);
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
        Call<ArrayList<JobBasic>> call = iJobsService.getJobApplied();
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