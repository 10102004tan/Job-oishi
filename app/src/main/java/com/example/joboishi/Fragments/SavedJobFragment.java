package com.example.joboishi.Fragments;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.databinding.FragmentSavedJobBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SavedJobFragment extends Fragment {

    private FragmentSavedJobBinding binding;
    private ArrayList<Job> jobList;
    private JobAdapter adapter;

    private IJobsService iJobsService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobBinding.inflate(inflater, container, false);
        this.jobList = new ArrayList<>();
        getJobsTest();
        return binding.getRoot();
    }


    private void getJobsTest(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        iJobsService = retrofit.create(IJobsService.class);
        Call<ArrayList<Job>> call = iJobsService.getListJobs();
        call.enqueue(new Callback<ArrayList<Job>>() {
            @Override
            public void onResponse(Call<ArrayList<Job>> call, Response<ArrayList<Job>> response) {
                if (response.isSuccessful()){
                    jobList = response.body();
                    adapter = new JobAdapter(jobList,getContext());
                    binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    binding.listJob.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Job>> call, Throwable t) {
                Log.d("error",t.getMessage()+"");
            }
        });
    }
}