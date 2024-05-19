package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joboishi.Api.UserFcmAPI;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.BaseActivity;
import com.example.joboishi.databinding.SplashLayoutBinding;
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

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {
    private UserFcmAPI userFcmAPI;
    private String userEmail;

    private SplashLayoutBinding binding;
    private int statusInternet = 0;
    private int userId;
    private CuteDialog.withIcon dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("user_email", "ko");
        userId = sharedPref.getInt("user_id", 0);
        processingTokenFcm();
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
                        sendRegistrationToServer(userId, token, userEmail);
                    }
                });
    }

    // Phương thức gửi token đã được tách ra và tối ưu
    private void sendRegistrationToServer(int userId, String token, String userEmail) {

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
                if (response.isSuccessful() && userId != 0) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if(userId == 0) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
                else{
//                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.lottieAnimationView.pauseAnimation();
                dialog =
                        new CuteDialog.withIcon(SplashActivity.this)
                                .setIcon(R.drawable.logo)
                                .setTitle("Lỗi")
                                .hideCloseIcon(true)
                                .setDescription(statusInternet == 0 ? "Không có kết nối mạng" : "Đã xảy ra lỗi, vui lòng thử laị sau")
                                .setPositiveButtonText("Tải lại", v2 -> {
                                    binding.lottieAnimationView.playAnimation();
                                    processingTokenFcm();
                                })
                                .setNegativeButtonText("Thoát", v1 -> {
                                    finish();
                                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
    @Override
    protected void handleNoInternet() {
        statusInternet = 0;
    }

    @Override
    protected void handleLowInternet() {
    }

    @Override
    protected void handleGoodInternet() {
        statusInternet = 1;
    }
}