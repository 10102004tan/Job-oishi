package com.example.joboishi.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.joboishi.Adapters.ViewPagerHomeAdapter;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.joboishi.Fragments.HomeFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.Fragments.ProfileFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;


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
        // Cập nhật UI của Fragment nếu cần

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