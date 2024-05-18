package com.example.joboishi.Fragments;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.joboishi.Activities.HomeActivity;
import com.example.joboishi.Activities.SearchActivity;
import com.example.joboishi.Activities.SearchResultActivity;
import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;

import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.BaseFragment;
import com.example.joboishi.databinding.FragmentHomeBinding;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;
public class HomeFragment extends BaseFragment {
    private IJobsService iJobsService;
    private JobAdapter adapter;
    private ArrayList<JobBasic> jobList;
    private FragmentHomeBinding binding;
    private SelectFilterJob selectFilterJob =  new SelectFilterJob();
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private ArrayList<String> filterJob;
    private boolean isNotification = false;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private static int page = 1;
    private ArrayList<Integer> arrId;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.imageNoInternet.setAnimation(R.raw.fetch_api_loading);

        //get All array id bookmark
        arrId = new ArrayList<>();
        getAllIdBookmark(3);

        //Start init
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        jobList = new ArrayList<>();
        //End init


        adapter = new JobAdapter(jobList, getContext());
        adapter.setArrId(arrId);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.listJob.setAdapter(adapter);
        getJobs();

        //Get event for button sheet

        binding.btnShowType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                selectFilterJob.setTitleFilter("Công việc ưu thích");
                filterJob = new ArrayList<>(Arrays.asList("Android Developer", "Frontend Developer", "Backend Developer"));
                selectFilterJob.setList(filterJob);
            }
        });

        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                selectFilterJob.setTitleFilter("Đia điểm");
                filterJob = new ArrayList<>(Arrays.asList("Thành Phố Hồ Chí Minh"));
                selectFilterJob.setList(filterJob);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });




        //refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJobs();
                getAllIdBookmark(3);
            }
        });

        //processing add bookmark
        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(int id) {
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("JOB_ID", id);
                startActivity(intent);
            }

            @Override
            public void onAddJobBookmark(JobBasic job,ImageView bookmarkImage) {
                saveJobToBookmarks(job);
                bookmarkImage.setSelected(true);
            }

            @Override
            public void onRemoveBookmark(JobBasic job, ImageView bookmarkImage) {
                removeJobBookmark(3, job.getId());
                bookmarkImage.setSelected(false);
            }
        });

        //processing load more
        adapter.setOnLoadMoreListener(new JobAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page+=1;
                Toast.makeText(getContext(), "Dang tai ...", Toast.LENGTH_SHORT).show();
                Call<ArrayList<JobBasic>> call = iJobsService.getListJobs(page);
                call.enqueue(new Callback<ArrayList<JobBasic>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                        if (response.isSuccessful()) {
                            jobList.addAll(response.body());
                            binding.imageNoInternet.setVisibility(View.GONE);
                            adapter.updateData(jobList);
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
                adapter.setLoading(false);
            }
        });


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
                    getJobs();
                    getAllIdBookmark(3);
                    binding.listJob.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();


    }

    //Button sheet here
    private void showDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(selectFilterJob);
        myBottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");
    }

    private void getJobs() {
        binding.imageNoInternet.setVisibility(View.VISIBLE);
        Call<ArrayList<JobBasic>> call = iJobsService.getListJobs(page);
        call.enqueue(new Callback<ArrayList<JobBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                if (response.isSuccessful()) {
                    jobList = response.body();
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.imageNoInternet.setVisibility(View.GONE);
                    adapter.updateData(jobList);
                    Log.d("testss",jobList.size()+"");
                }
            }
            @Override
            public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                binding.swipeRefreshLayout.setRefreshing(false);
                Log.d("testss",t.getMessage());
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
            getJobs();
            binding.imageNoInternet.setVisibility(View.GONE);
        }
    }

    private void saveJobToBookmarks(JobBasic job) {
        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks");
        String userId = "3"; // Lấy user ID từ SharedPreferences hoặc nơi lưu trữ khác
        bookmarksRef.child("userId"+userId).child("job"+job.getId()).setValue(job)
                .addOnSuccessListener(aVoid -> {
                    MotionToast.Companion.createToast(getActivity(), "😍",
                            "Đã thêm công việc thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    Log.d("test11",e.getMessage());
                    Toast.makeText(getContext(), "Lỗi khi thêm vào bookmark "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeJobBookmark(int userId, int jobId) {
        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks");
        bookmarksRef.child("userId"+userId).child("job"+jobId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    MotionToast.Companion.createToast(getActivity(), "😍",
                            "Đã xoa công việc thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    Log.d("test11",e.getMessage());
                    Toast.makeText(getContext(), "Lỗi khi xóa khỏi bookmark "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void getAllIdBookmark(int userId){
        arrId.clear();
        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks").child("userId3");
        bookmarksRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        JobBasic job = data.getValue(JobBasic.class);
                        arrId.add(job.getId());
                    }
                } else {
                    Log.d("test11",task.getException().getMessage());
                }
            }
        });
    }
}