package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Api.JobBookmarksResponse;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;

import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.Abstracts.BaseFragment;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.databinding.FragmentSavedJobBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SavedJobFragment extends BaseFragment {
    private FragmentSavedJobBinding binding;
    private ArrayList<JobBasic> jobs;
    private JobAdapter adapter;
    private HomeViewModel homeViewModel;

    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private IJobsService iJobsService;
    private int userId;
    private int page = 1;
    private int totalPages = 1;
    private boolean isDestroy = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getCurrentTotalBookmark().observe(getViewLifecycleOwner(), total -> {
            if (!isDestroy){
                jobs.clear();
                getJobsSaved();
            }
            if (total == 0){
                binding.image.setVisibility(View.VISIBLE);
                binding.image.setAnimation(R.raw.no_data);
                binding.image.playAnimation();
                binding.listJob.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobBinding.inflate(inflater, container, false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", userId);

        iJobsService = retrofit.create(IJobsService.class);
        jobs = new ArrayList<>();
        adapter = new JobAdapter(jobs,getContext());
        adapter.setBookmark(true);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.listJob.setAdapter(adapter);
        //Add event for adapter
        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(JobBasic job) {
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("JOB_ID",job.getId());
                intent.putExtra("TYPE",job.getType());
                startActivity(intent);
            }

            @Override
            public void onRemoveBookmark(JobBasic jobBasic, ImageView bookmarkImage, int position) {
                removeJobBookmark(userId,jobBasic.getId());
                jobs.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        //Add event for swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobs.clear();
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                page = 1;
                getJobsSaved();
                if (statusInternet == STATUS_NO_INTERNET){
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.image.setAnimation(R.raw.a404);
                    binding.image.playAnimation();
                }
            }
        });

        //processing load more
        binding.idNestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    if (page <= totalPages) {
                        binding.idPBLoading.setVisibility(View.VISIBLE);
                        getJobsSaved();
                    }
                }
            }
        });
        return binding.getRoot();
    }
    //get data from server
    private void getJobsSaved(){

        if (page == 1){
            binding.listJob.setVisibility(View.GONE);
            binding.image.setVisibility(View.VISIBLE);
            binding.image.setAnimation(R.raw.fetch_api_loading);
            binding.image.playAnimation();
            binding.idPBLoading.setVisibility(View.GONE);
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        iJobsService = retrofit.create(IJobsService.class);
        Call<JobBookmarksResponse> call = iJobsService.getJobBookmark(userId,page);
        call.enqueue(new Callback<JobBookmarksResponse>() {
            @Override
            public void onResponse(Call<JobBookmarksResponse> call, Response<JobBookmarksResponse> response) {
                JobBookmarksResponse jobBookmarksResponse = response.body();
                for(JobBasic job : jobBookmarksResponse.getData()){
//                    job.setBookmarked(true);
                    jobs.add(job);
                }
                totalPages = jobBookmarksResponse.getTotalPages();
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.idPBLoading.setVisibility(View.GONE);
                if (jobs.size() == 0){
                    binding.image.setVisibility(View.VISIBLE);
                    binding.image.setAnimation(R.raw.no_data);
                    binding.image.playAnimation();
                    binding.listJob.setVisibility(View.GONE);
                }
                else{
                    binding.listJob.setVisibility(View.VISIBLE);
                    binding.image.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<JobBookmarksResponse> call, Throwable t) {

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
        }

        MotionToast.Companion.createToast(getActivity(), "😍",
                getString(R.string.kh_ng_c_k_t_n_i_m_ng),
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
    }

    @Override
    protected void handleLowInternet() {
        MotionToast.Companion.createToast(getActivity(), "😍",
                getString(R.string.ang_k_t_n_i),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
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
    private void removeJobBookmark(int userId, int jobId) {
        Call<Void> call = iJobsService.destroyBookmark(userId, jobId, 0);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    isDestroy = true;
                    homeViewModel.setCurrentTotalBookmark(homeViewModel.getCurrentTotalBookmark().getValue()-1);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isDestroy){
            isDestroy = false;
        }
    }
}