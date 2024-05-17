package com.example.joboishi.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

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


public class HomeActivity extends AppCompatActivity implements HomeFragment.IHomeFragment {
    private static final int REQ = 111111;
    private HomeLayoutBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private MyJobFragment myJobFragment;
    private boolean hasRequestedPermission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        myJobFragment = new MyJobFragment();

        fragmentManager = getSupportFragmentManager();
        switchFragment(homeFragment);
        activeFragment = homeFragment;
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    switchFragment(homeFragment);
                } else if (item.getItemId() == R.id.nav_my_jobs) {
                    switchFragment(myJobFragment);
                } else if (item.getItemId() == R.id.nav_profile) {
                    switchFragment(profileFragment);
                }
                return true;
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        if (fragment != activeFragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // Ẩn Fragment hiện tại
            if (activeFragment != null) {
                transaction.hide(activeFragment);
            }
            // Hiển thị Fragment mới hoặc thêm nếu chưa tồn tại
            if (fragment.isAdded()) {
                transaction.show(fragment);
            } else {
                transaction.add(R.id.fragmentHomeLayout, fragment);
            }
            transaction.commit();
            activeFragment = fragment;
        }
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
        if (homeFragment != null) {
            homeFragment.setNotification(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermission(Manifest.permission.POST_NOTIFICATIONS) && !hasRequestedPermission) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ);
            hasRequestedPermission = true;
        } else {
            doWithPermission();
        }
    }

    @Override
    public void onSwitchNotification(boolean isNotification) {
        Intent intent = new Intent();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        }
        startActivity(intent);
    }
}