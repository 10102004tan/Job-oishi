package com.example.joboishi.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.example.joboishi.Adapters.ViewPagerHomeAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joboishi.Api.UserFcmAPI;
import com.example.joboishi.R;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);


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
           else if (item.getItemId() == R.id.nav_profile){
               binding.viewPager.setCurrentItem(2);
           }
           return true;
        });

    }



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
}