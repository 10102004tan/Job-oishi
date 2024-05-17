package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.joboishi.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // // Lấy giá trị từ SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                String userEmail = sharedPref.getString("user_email", null);

                if (userEmail != null) {
                    // Sử dụng userEmail
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                    Log.d("UserInfo", "Email Người Dùng: " + userEmail);
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    // Xử lý trường hợp không có giá trị lưu trữ
                    Log.d("UserInfo", "Không tìm thấy email người dùng");
                }


            }
        }, SPLASH_TIME_OUT);
    }
}