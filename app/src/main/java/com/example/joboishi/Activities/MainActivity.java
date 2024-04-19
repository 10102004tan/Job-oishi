package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.joboishi.Adapters.ViewPagerHomeAdapter;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.HomeLayoutBinding;

public class MainActivity extends AppCompatActivity {

    private HomeLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewPagerHomeAdapter adapter = new ViewPagerHomeAdapter(this);
        binding.fragmentHomeLayout.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.fragmentHomeLayout.setAdapter(adapter);
    }
}