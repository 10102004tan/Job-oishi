package com.example.joboishi.Activities;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.joboishi.Adapters.ViewPagerHomeAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joboishi.R;
import com.example.joboishi.databinding.HomeLayoutBinding;


public class HomeActivity extends AppCompatActivity{

    private static final int REQ = 111111;
    private HomeLayoutBinding binding;
    private boolean hasPermission = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
}