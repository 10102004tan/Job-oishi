package com.example.joboishi.Fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.joboishi.Api.JobsResponse;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.Abstracts.BaseFragment;
import com.example.joboishi.databinding.FragmentHomeTopDevBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class HomeTopDevFragment extends BaseFragment {

    private IJobsService iJobsService;
    private static final int TYPE = 0;
    private JobAdapter adapter;
    static final int REQUEST_CODE = 1;
    private ArrayList<JobBasic> jobList;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private static int page = 1;
    private int lastPage = 1;

    private FragmentHomeTopDevBinding binding;
    private int userId;
    private String city;
    private HomeViewModel homeViewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getSelectedValueTopDev().observe(getViewLifecycleOwner(), str -> {
            city = (str == null || str == "Tất cả") ? "" : str.toLowerCase().trim();
            page = 1;
            binding.swipeRefreshLayout.setRefreshing(true);
            jobList.clear();
            binding.idNestedSV.smoothScrollTo(0,0);
            getJobs(city);
        });

        homeViewModel.getCurrentTabPosition().observe(getViewLifecycleOwner(), position -> {
            if (position != null && position == -1) {
                binding.idNestedSV.smoothScrollTo(0,0);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeTopDevBinding.inflate(inflater, container, false);
        //get user id
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", 0);
        //get All array id bookmark

        //Start init
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        jobList = new ArrayList<>();
        //End init@


        adapter = new JobAdapter(jobList, getContext());
        adapter.setVisibleBookmark(false);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.listJob.setAdapter(adapter);
        getJobs(null);
        //refresh
        //Add event for swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                if (statusPreInternet == STATUS_NO_INTERNET){
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.listJob.setVisibility(View.GONE);
                    binding.imageNoInternet.setVisibility(View.VISIBLE);
                }
                else{
                    binding.listJob.setVisibility(View.VISIBLE);
                }
                jobList.clear();
                page = 1;
                getJobs(city);
            }
        });
        adapter.setVisibleBookmark(false);
        //processing add bookmark
        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(JobBasic job) {
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("JOB_ID", job.getId());
                intent.putExtra("TYPE",TYPE);
                getActivity().startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onRemoveBookmark(JobBasic jobBasic, ImageView bookmarkImage, int position) {
                //khong lam gi
            }
        });
        //processing load more
        binding.idNestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    binding.idPBLoading.setVisibility(View.VISIBLE);
                    if (page <= lastPage) {
                        getJobs(city);
                    }
                }
            }
        });

        return binding.getRoot();
    }
    private void getJobs(String city) {
        city = (city == null) ? "" : city;
        // neu la lan dau tien va khong phai refresh
        if (page == 1 && !binding.swipeRefreshLayout.isRefreshing()) {
            binding.imageNoInternet.setAnimation(R.raw.fetch_api_loading);
            binding.imageNoInternet.playAnimation();
            binding.imageNoInternet.setVisibility(View.VISIBLE);
            binding.idPBLoading.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setRefreshing(false);
        }

        Call<JobsResponse> call = iJobsService.getListJobs(city, page, userId,TYPE);
        call.enqueue(new Callback<JobsResponse>() {
            @Override
            public void onResponse(Call<JobsResponse> call, Response<JobsResponse> response) {
                if (response.isSuccessful()) {
                    JobsResponse result = response.body();
                    assert result != null;
                    jobList.addAll((ArrayList<JobBasic>)result.getListJobs());
                    lastPage = result.getLastPage();
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.imageNoInternet.setVisibility(View.GONE);
                    binding.idPBLoading.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<JobsResponse> call, Throwable t) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void handleNoInternet() {
        statusPreInternet = STATUS_NO_INTERNET;
        if (isFirst) {
            statusInternet = STATUS_NO_INTERNET;
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.listJob.setVisibility(View.GONE);
            binding.imageNoInternet.setAnimation(R.raw.a404);
            binding.imageNoInternet.playAnimation();
            binding.imageNoInternet.setVisibility(View.VISIBLE);
            isFirst = false;
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
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
    }

    @Override
    protected void handleGoodInternet() {
        statusPreInternet = STATUS_GOOD_INTERNET;
        binding.listJob.setVisibility(View.VISIBLE);
        if (isFirst) {
            isFirst = false;
        }else{
            page = 1;
            getJobs(null);
            binding.imageNoInternet.setVisibility(View.GONE);
        }
    }
}
