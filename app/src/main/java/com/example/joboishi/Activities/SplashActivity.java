package com.example.joboishi.Activities;

import android.animation.Animator;
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

    private SplashLayoutBinding binding;
    private int statusInternet = 0;
    private int userId;
    private CuteDialog.withIcon dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog =
                new CuteDialog.withIcon(SplashActivity.this)
                        .setIcon(R.drawable.logo)
                        .setTitle("Lỗi")
                        .hideCloseIcon(true)
                        .setDescription(statusInternet == 0 ? "Không có kết nối mạng" : "Đã xảy ra lỗi, vui lòng thử laị sau")
                        .setPositiveButtonText("Tải lại", v2 -> {
                            binding.lottieAnimationView.playAnimation();
                        })
                        .setNegativeButtonText("Thoát", v1 -> {
                            finish();
                        });
        dialog.setCanceledOnTouchOutside(false);


        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);

        //Handler 3s for animtion
        binding.lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (statusInternet != 0) {
                    if (userId != 0) {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
                else{
                    dialog.show();

                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });

        binding.lottieAnimationView.playAnimation();


    }

    @Override
    protected void handleNoInternet() {
        statusInternet = 0;
        dialog.show();

    }

    @Override
    protected void handleLowInternet() {
    }

    @Override
    protected void handleGoodInternet() {
        statusInternet = 1;
    }
}