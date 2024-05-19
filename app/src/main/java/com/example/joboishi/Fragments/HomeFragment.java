package com.example.joboishi.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.joboishi.Activities.SearchActivity;
import com.example.joboishi.Adapters.ViewPagerHomeFragmentAdapter;

import com.example.joboishi.R;
import com.example.joboishi.ViewModels.ScrollRecyclerviewListener;
import com.example.joboishi.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.vdx.designertoast.DesignerToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;
public class HomeFragment extends Fragment {

    private ArrayList<String> filterJob;

    private SelectFilterJob selectFilterJob =  new SelectFilterJob();
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private FragmentHomeBinding binding;
    private ScrollRecyclerviewListener scrollRecyclerviewListener;
    private boolean isFirstTimeSelected = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        //viewpager2
        ViewPagerHomeFragmentAdapter viewPagerHomeAdapter = new ViewPagerHomeFragmentAdapter(this);
        binding.viewPager.setAdapter(viewPagerHomeAdapter);

        /*
         *ADD ITEM ON TABLAYOUT
         */

        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText("Job Oishii"));
        binding.tabLayoutMyJob.addTab(binding.tabLayoutMyJob.newTab().setText("Top Dev"));


        /*ADD LISTENER FOR tablayout*/
        binding.tabLayoutMyJob.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                binding.viewPager.setCurrentItem(position);
                isFirstTimeSelected = false;
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                isFirstTimeSelected = true;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (!isFirstTimeSelected){
                    scrollRecyclerviewListener.setCurrentTabPosition(-1);
                }
            }
        });

        filterJob = new ArrayList<>(Arrays.asList("Android Developer", "Frontend Developer", "Backend Developer"));
        /*DONG BO TABLELAYOUT VOI VIEWPAGER*/
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayoutMyJob.selectTab(binding.tabLayoutMyJob.getTabAt(position));
            }
        });

        binding.btnShowType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                selectFilterJob.setTitleFilter("Tiêu chí công việc");
                selectFilterJob.setList(new ArrayList<>(Arrays.asList("Phù hợp nhất", "Lương cao")));
            }
        });

        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

                selectFilterJob.setTitleFilter("Đia điểm");
                selectFilterJob.setList(new ArrayList<>(Arrays.asList("Thành Phố Hồ Chí Minh", "TP. Hà Nội", "Đà Nẵng")));
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("filterJob", filterJob);
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