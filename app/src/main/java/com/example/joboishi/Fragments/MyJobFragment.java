package com.example.joboishi.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joboishi.Adapters.ViewPagerHomeAdapter;
import com.example.joboishi.Adapters.ViewPagerMyJobAdpater;
import com.example.joboishi.R;
import com.example.joboishi.databinding.FragmentMyJobBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyJobFragment extends Fragment {
    private FragmentMyJobBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyJobBinding.inflate(inflater, container, false);
        ViewPagerMyJobAdpater adapter = new ViewPagerMyJobAdpater(this);
        binding.viewPagerMyJob.setAdapter(adapter);

        /*
        *ADD ITEM ON TABLAYOUT
        */
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText("Da ung tuyen"));
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText("Viec da luu"));
        /*ADD LISTENER FOR tablayout*/
        binding.tabLayoutMyJob.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPagerMyJob.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*DONG BO TABLAYOUT VOI VIEWPAGER*/
        binding.viewPagerMyJob.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayoutMyJob.selectTab(binding.tabLayoutMyJob.getTabAt(position));
            }
        });
        return binding.getRoot();
    }

}