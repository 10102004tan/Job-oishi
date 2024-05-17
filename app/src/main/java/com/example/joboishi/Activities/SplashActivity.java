package com.example.joboishi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserFcmAPI;
import com.example.joboishi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private UserFcmAPI userFcmAPI;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("user_email", null);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", true); // Mặc định là false
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        int userId = 2;
                        // Lưu token vào db
                        sendRegistrationToServer(userId,token,userEmail);
                    }
                });
    }

    // Phương thức gửi token đã được tách ra và tối ưu
    private void sendRegistrationToServer(int userId,String token,String userEmail) {

        // Chỉ khởi tạo Retrofit một lần (nếu chưa được khởi tạo)
        if (userFcmAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(UserFcmAPI.BASE_URL)
                    .build();
            userFcmAPI = retrofit.create(UserFcmAPI.class);
        }

        Call<ResponseBody> call = userFcmAPI.sendFcmToken(userId, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && userEmail != null) {
                   Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                   startActivity(intent);
                } else if(userEmail == null) {
                  startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    
                }
              else{
                  // Xử lý khi gửi token không thành công (ví dụ: kiểm tra mã lỗi)
                    Log.e("testsss", "Send FCM token failed with code: " + response.code());
              }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("testsss", "Send FCM token failed", t);
            }
        });
    }
}