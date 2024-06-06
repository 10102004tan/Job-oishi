package com.example.joboishi.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.joboishi.Activities.SearchActivity;
import com.example.joboishi.Adapters.ViewPagerHomeFragmentAdapter;

import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment  {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private boolean isFirstTimeSelected = true;

    private boolean isLoadFirst = true;
    private SelectFilterJob selectFilterJob =  new SelectFilterJob();
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tabLayoutMyJob.setTabIndicatorFullWidth(true);

        /*SET VALUE FOR BUTTON*/
        if (isLoadFirst) {
            homeViewModel.getSelectedValueJoboishi().observe(getViewLifecycleOwner(), str -> {
                binding.btnLocation.setText(str);
            });
            isLoadFirst = false;
        }

        /*ADD LISTENER FOR tablelayout*/
        binding.tabLayoutMyJob.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                binding.viewPager.setCurrentItem(position);
                homeViewModel.setCurrentTabPosition(position);
                isFirstTimeSelected = false;
                //set current location
                if (position == 0) {
                    homeViewModel.getSelectedValueJoboishi().observe(getViewLifecycleOwner(), str -> {
                        binding.btnLocation.setText(str);
                    });
                } else {
                    homeViewModel.getSelectedValueTopDev().observe(getViewLifecycleOwner(), str -> {
                        binding.btnLocation.setText(str);
                    });
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                isFirstTimeSelected = true;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (!isFirstTimeSelected){
                    homeViewModel.setCurrentTabPosition(-1);
                }
            }
        });

        /*DONG BO TABLELAYOUT VOI VIEWPAGER*/
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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        //viewpager2
        ViewPagerHomeFragmentAdapter viewPagerHomeAdapter = new ViewPagerHomeFragmentAdapter(this);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAdapter(viewPagerHomeAdapter);

        /*
         *ADD ITEM ON TABLELAYOUT
         */
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText(R.string.job_oishi));
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText(R.string.top_dev));


        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                selectFilterJob.setTitleFilter("Đia điểm");
                selectFilterJob.setList(new ArrayList<>(Arrays.asList("Tất cả" ,"Thành phố Hồ Chí Minh", "Thành phố Hà Nội", "Thành phố Đà Nẵng")));
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);

                intent.putExtra("filterJob", new ArrayList<>(Arrays.asList("Android", "Frontend", "Backend", "Java")));
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void showDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(selectFilterJob);
        myBottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");

    }

}