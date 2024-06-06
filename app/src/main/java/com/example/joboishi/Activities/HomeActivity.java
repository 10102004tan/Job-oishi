package com.example.joboishi.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joboishi.Adapters.NotificationAdapter;
import com.example.joboishi.Adapters.ViewPagerHomeAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Api.INotificationService;
import com.example.joboishi.Api.JobBookmarksResponse;
import com.example.joboishi.Api.NotificationResponse;
import com.example.joboishi.Api.UserFcmAPI;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.Services.MyFirebaseMessagingService;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import io.github.cutelibs.cutedialog.CuteDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class HomeActivity extends AppCompatActivity{
    private static final int REQ = 111111;
    private int userId;
    private HomeLayoutBinding binding;
    private boolean hasPermission = false;
    private UserFcmAPI userFcmAPI;
    private INotificationService iNotificationService;

    private HomeViewModel homeViewModel;
    private int totalBookmark = 0;
    private int totalJobApplied = -1;
    private IJobsService iJobsService;
    private NotificationResponse notificationResponse;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        getTotalBookmark(userId);
        getTotalJobApplied(userId);
        getNotifications(userId);



        //viewpager2
        ViewPagerHomeAdapter viewPagerHomeAdapter = new ViewPagerHomeAdapter(this);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAdapter(viewPagerHomeAdapter);
        //bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
           if (item.getItemId() == R.id.nav_home){
               binding.viewPager.setCurrentItem(0);
           }
           else if (item.getItemId() == R.id.nav_my_jobs){
               binding.viewPager.setCurrentItem(1);
           }
           else if (item.getItemId() == R.id.nav_noti){
               binding.viewPager.setCurrentItem(2);
           }
           else if (item.getItemId() == R.id.nav_profile){
               binding.viewPager.setCurrentItem(3);
           }
           return true;
        });

        //lister fcm
        MyFirebaseMessagingService.setOnMessageReceivedListener(new MyFirebaseMessagingService.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String title, String body) {
                getNotifications(userId);
            }
        });
    }
    //onCreateViewed

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ && permissions.length == grantResults.length) {
            for (int check : grantResults) {
                if (check != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            doWithPermission();
        }
    }

    private boolean checkPermission(String permission) {
        int check = checkSelfPermission(permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void doWithPermission() {
        processingTokenFcm();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermission(Manifest.permission.POST_NOTIFICATIONS) && !hasPermission) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ);
            hasPermission = true;
        }else{
            doWithPermission();
        }
    }
    private void processingTokenFcm() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        // Lưu token vào db
                        sendRegistrationToServer(userId, token);
                    }
                });
    }
    // Phương thức gửi token đã được tách ra và tối ưu
    private void sendRegistrationToServer(int userId, String token) {
        // Chỉ khởi tạo Retrofit một lần (nếu chưa được khởi tạo)
        if (userFcmAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(UserFcmAPI.BASE_URL)
                    .build();
            userFcmAPI = retrofit.create(UserFcmAPI.class);
            Call<ResponseBody> call = userFcmAPI.sendFcmToken(userId, token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
    private void getTotalBookmark(int userId){
        Log.d("test111", "getTotalBookmark: ");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        Call<Integer> call = iJobsService.getTotalBookmark(userId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()){
                    totalBookmark = response.body();
                    homeViewModel.setCurrentTotalBookmark(totalBookmark);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
    private void getTotalJobApplied(int userId){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        Call<Integer> call = iJobsService.getTotalJobsApplied(userId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()){
                    totalJobApplied = response.body();
                    homeViewModel.setCurrentTotalApplied(totalJobApplied);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    private void getNotifications(int userId) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iNotificationService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iNotificationService = retrofit.create(INotificationService.class);
        Call<NotificationResponse> call = iNotificationService.getAllNotifications(userId);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    notificationResponse = response.body();
                    homeViewModel.setNotificationResponse(notificationResponse);
                    String countNotRead = (notificationResponse.getTotalNotRead() > 9 ? "9+" : notificationResponse.getTotalNotRead() + "");
                    Menu menu = binding.bottomNavigationView.getMenu();
                    MenuItem menuItem = menu.findItem(R.id.nav_noti);
                    if (notificationResponse.getTotalNotRead() == 0)
                        menuItem.setTitle("Thông báo");
                    else{
                        menuItem.setTitle("Thông báo (" + countNotRead + ")");
                    }
                }
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTotalBookmark(userId);
        getTotalJobApplied(userId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11111 && resultCode == RESULT_OK){
            getNotifications(userId);
            Log.d("HomeActivity", "onActivityResult: ");
        }
    }
}