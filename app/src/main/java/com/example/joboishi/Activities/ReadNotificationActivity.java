package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.joboishi.Api.APIURL;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.INotificationService;
import com.example.joboishi.Models.Notification;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ReadNotificationLayoutBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReadNotificationActivity extends AppCompatActivity {
    private ReadNotificationLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReadNotificationLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy giá trị từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("user_id", 0);

        // get notification from intent
        Intent intent = getIntent();
        Notification notification = (Notification) intent.getSerializableExtra("notification");

        if (notification != null) {
            binding.txtTitle.setText(notification.getTitle());
            binding.txtContent.setText(notification.getMessage());
            readed(notification.getId(),userId);
        }
    }

    private void readed(int notificationId,int userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIURL.BASE_URL)
                .build();
        INotificationService iNotificationService = retrofit.create(INotificationService.class);
        Call<Void> call = iNotificationService.readed(notificationId,userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
               if (response.isSuccessful()){
                   Log.d("ReadNotificationActivity", "onResponse: "+response.message());
                   Intent result = new Intent();
                   setResult(RESULT_OK, result);
               }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("ReadNotificationActivity", "onFailure: "+t.getMessage());
            }
        });

    }
}