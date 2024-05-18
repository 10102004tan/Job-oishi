package com.example.joboishi.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.joboishi.Adapters.ViewPagerHomeFragmentAdapter;

import com.example.joboishi.ViewModels.ScrollRecyclerviewListener;
import com.example.joboishi.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment  {



    private FragmentHomeBinding binding;
    private ScrollRecyclerviewListener scrollRecyclerviewListener;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*ADD LISTENER FOR tablayout*/
        binding.tabLayoutMyJob.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                scrollRecyclerviewListener.setCurrentTabPosition(position);
                binding.viewPager.setCurrentItem(position);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                scrollRecyclerviewListener.setCurrentTabPosition(-1);
            }
        });

        /*DONG BO TABLAYOUT VOI VIEWPAGER*/
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayoutMyJob.selectTab(binding.tabLayoutMyJob.getTabAt(position));
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        scrollRecyclerviewListener = new ViewModelProvider(requireActivity()).get(ScrollRecyclerviewListener.class);

        //viewpager2
        ViewPagerHomeFragmentAdapter viewPagerHomeAdapter = new ViewPagerHomeFragmentAdapter(this);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAdapter(viewPagerHomeAdapter);

        /*
         *ADD ITEM ON TABLAYOUT
         */
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText("Job oishi"));
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText("Top dev"));



        return binding.getRoot();
    }

}