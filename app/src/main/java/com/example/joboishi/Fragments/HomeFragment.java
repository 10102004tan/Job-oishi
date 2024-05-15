package com.example.joboishi.Fragments;

import android.Manifest;
import android.content.Context;
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

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Models.Job;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.databinding.FragmentHomeBinding;
import com.example.joboishi.databinding.FragmentSavedJobBinding;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class HomeFragment extends Fragment {
    private IJobsService iJobsService;
    private JobAdapter adapter;
    private ArrayList<JobBasic> jobList;
    private FragmentHomeBinding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NOTIFICATION_ENABLED = "notification_enabled";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentHomeBinding.inflate(inflater, container, false);

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        boolean isNotificationEnabled = sharedPreferences.getBoolean(PREF_NOTIFICATION_ENABLED, false); // Mặc định là false
        //processing switch notification
        binding.switchNotification.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    askNotificationPermission();
                } else {
                    disableNotifications();
                }
            }
        });


        // Request permission
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                });
        askNotificationPermission();
        //register
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                    }
                });

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

            }

            @Override
            public void onClickBookmark(JobBasic job) {
                //processing post job add bookmark
                Call<JobBasic> call = iJobsService.addJobToBookmark(job);
                call.enqueue(new Callback<JobBasic>() {
                    @Override
                    public void onResponse(@NonNull Call<JobBasic> call, @NonNull Response<JobBasic> response) {
                        if (response.isSuccessful()) {
                            MotionToast.Companion.createToast(getActivity(), "😍",
                                    "Đã thêm công việc thành công",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                        } else {
                            MotionToast.Companion.createToast(getActivity(), "☹\uFE0F",
                                    "Bạn đã thêm công việc này rồi",
                                    MotionToastStyle.WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                        }
                    }

                    @Override
                    public void onFailure(Call<JobBasic> call, Throwable t) {
                        MotionToast.Companion.createToast(getActivity(), "☹\uFE0F",
                                "Thử lại sau",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                    }
                });
            }
        });



        //processing load more
        adapter.setOnLoadMoreListener(new JobAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                //create handler to delay 1s
                binding.listJob.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("testaaa", "Test");
                        jobList.add(new JobBasic(11111, "title", "company", "address", "https://marketplace.canva.com/EAE85VgPq3E/1/0/1600w/canva-v%E1%BA%BD-tay-h%C3%ACnh-tr%C3%B2n-logo-c3Jw1yOiXJw.jpg", "logo", false,"test"));
                        jobList.add(new JobBasic(11111, "title 1", "company", "address", "https://marketplace.canva.com/EAE85VgPq3E/1/0/1600w/canva-v%E1%BA%BD-tay-h%C3%ACnh-tr%C3%B2n-logo-c3Jw1yOiXJw.jpg", "logo", false,"test"));
                        jobList.add(new JobBasic(11111, "title 2", "company", "address", "https://marketplace.canva.com/EAE85VgPq3E/1/0/1600w/canva-v%E1%BA%BD-tay-h%C3%ACnh-tr%C3%B2n-logo-c3Jw1yOiXJw.jpg", "logo", false,"test"));
                        jobList.add(new JobBasic(11111, "title 3", "company", "address", "https://marketplace.canva.com/EAE85VgPq3E/1/0/1600w/canva-v%E1%BA%BD-tay-h%C3%ACnh-tr%C3%B2n-logo-c3Jw1yOiXJw.jpg", "logo", false,"test"));
                        jobList.add(new JobBasic(11111, "title 4", "company", "address", "https://marketplace.canva.com/EAE85VgPq3E/1/0/1600w/canva-v%E1%BA%BD-tay-h%C3%ACnh-tr%C3%B2n-logo-c3Jw1yOiXJw.jpg", "logo", false,"test"));
                        jobList.add(new JobBasic(11111, "title 5", "company", "address", "https://marketplace.canva.com/EAE85VgPq3E/1/0/1600w/canva-v%E1%BA%BD-tay-h%C3%ACnh-tr%C3%B2n-logo-c3Jw1yOiXJw.jpg", "logo", false,"test"));
                        adapter.updateData(jobList);
                    }
                }, 1000);
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

    private void askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            enableNotifications();
        }
        else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }


    private void enableNotifications() {
        sharedPreferences.edit().putBoolean(PREF_NOTIFICATION_ENABLED, true).apply();
        // Gọi API hoặc thực hiện các tác vụ cần thiết để đăng ký nhận thông báo ở đây
    }

    private void disableNotifications() {
        sharedPreferences.edit().putBoolean(PREF_NOTIFICATION_ENABLED, false).apply();
        // Gọi API hoặc thực hiện các tác vụ cần thiết để hủy đăng ký nhận thông báo ở đây
    }

}