package com.example.joboishi.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.databinding.FragmentHomeBinding;
import com.example.joboishi.databinding.FragmentSavedJobBinding;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.google.gson.Gson;
import com.vdx.designertoast.DesignerToast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private IJobsService iJobsService;
    private JobAdapter adapter;
    private ArrayList<JobBasic> jobList;
    private FragmentHomeBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        jobList = new ArrayList<>();
        // Inflate the layout for this fragment
        adapter = new JobAdapter(jobList, getContext());
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.listJob.setAdapter(adapter);
        getJobs();

        //refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJobs();
            }
        });

        //processing add bookmark
        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(int id) {
                //Truyền Dữ liệu sang DetailJobActivity
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("JOB_ID", id);
                startActivity(intent);
            }

            @Override
            public void onClickBookmark(JobBasic job) {
                //processing post job add bookmark

                Call<JobBasic> call = iJobsService.addJobToBookmark(job);
                call.enqueue(new Callback<JobBasic>() {
                    @Override
                    public void onResponse(@NonNull Call<JobBasic> call, @NonNull Response<JobBasic> response) {
                        if (response.isSuccessful()) {

                            DesignerToast.Success(getContext(), "Lưu trữ thành công", Gravity.CENTER, Toast.LENGTH_SHORT);
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            Log.d("testssss", "onResponse: " + json);
                        } else {
                            DesignerToast.Error(getContext(), "Đã tồn tại", Gravity.CENTER, Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<JobBasic> call, Throwable t) {
                        Log.d("testssss", "onFailure: " + t.getMessage());
                        DesignerToast.Error(getContext(), "Thử lại sau", Gravity.CENTER, Toast.LENGTH_SHORT);
                    }
                });
            }
        });
        return binding.getRoot();
    }


    private void getJobs() {
        Call<ArrayList<JobBasic>> call = iJobsService.getListJobs();
        call.enqueue(new Callback<ArrayList<JobBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                if (response.isSuccessful()) {
                    jobList = response.body();
                    binding.swipeRefreshLayout.setRefreshing(false);
                    adapter.updateData(jobList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                Log.d("testaaa", "onFailure: " + t.getMessage());
            }
        });
    }
}