package com.example.joboishi.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.joboishi.Fragments.HomeFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.Fragments.ProfileFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.HomeLayoutBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private HomeLayoutBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             fragmentTransaction = fragmentManager.beginTransaction();
             if (item.getItemId() == R.id.nav_home){
                 fragmentTransaction.replace(R.id.fragmentHomeLayout, new HomeFragment());
                 fragmentTransaction.commit();
                 return true;
             }
             else if (item.getItemId() == R.id.nav_my_jobs){
                 fragmentTransaction.replace(R.id.fragmentHomeLayout, new MyJobFragment());
                 fragmentTransaction.commit();
                 return true;
             }
             else if (item.getItemId() == R.id.nav_profile){
                 fragmentTransaction.replace(R.id.fragmentHomeLayout, new ProfileFragment());
                 fragmentTransaction.commit();
                 return true;
             }
             return false;
         }
     });
    }
}