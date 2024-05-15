package com.example.joboishi.Activities;
import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.joboishi.Fragments.HomeFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.Fragments.ProfileFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {
    private HomeLayoutBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private MyJobFragment myJobFragment;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //register permission
        // Request permission
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Quyền được cấp, gửi thông báo
                    } else {
                        // Quyền bị từ chối, xử lý tương ứng (ví dụ: thông báo cho người dùng)
                    }
                });

        askNotificationPermission();

        //register
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Lấy mã thông báo FCM thất bại", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d("tannguyen1", "Mã thông báo FCM: " + token);
                        // Lưu trữ mã thông báo vào cơ sở dữ liệu hoặc chia sẻ với máy chủ của bạn
                    }
                });

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        myJobFragment = new MyJobFragment();

        fragmentManager = getSupportFragmentManager();
        switchFragment(homeFragment);
        activeFragment = homeFragment;
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    switchFragment(homeFragment);
                }
                else if (item.getItemId() == R.id.nav_my_jobs){
                    switchFragment(myJobFragment);
                }
                else if (item.getItemId() == R.id.nav_profile){
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

    // Declare the launcher at the top of your Activity/Fragment:
    private void askNotificationPermission() {
        // Chỉ cần thiết cho API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                // Giải thích cho người dùng lý do cần quyền (nếu cần)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}