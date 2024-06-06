package com.example.joboishi.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joboishi.Activities.ReadNotificationActivity;
import com.example.joboishi.Adapters.NotificationAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Api.INotificationService;
import com.example.joboishi.Api.NotificationResponse;
import com.example.joboishi.Models.Notification;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.databinding.FragmentNotificationBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private ArrayList<Notification> notificationList;
    private NotificationAdapter adapter;

    private int REQUEST_CODE = 11111;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel homeViewModel  = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getNotificationResponse().observe(getViewLifecycleOwner(), notificationResponse -> {
            notificationList = notificationResponse.getNotifications();
            adapter = new NotificationAdapter(notificationList);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Notification notification) {
                    Intent intent = new Intent(getContext(), ReadNotificationActivity.class);
                    intent.putExtra("notification", notification);
                    getActivity().startActivityForResult(intent, REQUEST_CODE);
                }
            });
            adapter.notifyDataSetChanged();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        notificationList = new ArrayList<>();



        return binding.getRoot();
    }
}