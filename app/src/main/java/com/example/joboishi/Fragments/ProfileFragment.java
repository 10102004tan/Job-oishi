package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.joboishi.Activities.JobCriteriaActivity;
import com.example.joboishi.Activities.LoginActivity;
import com.example.joboishi.Activities.ProfileActivity;
import com.example.joboishi.Activities.SettingActivity;
import com.example.joboishi.Activities.UploadFileActivity;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserResponse;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.ViewModels.LoadingDialog;
import com.example.joboishi.databinding.FragmentProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private final int REQUEST_CODE_TO_PROFILE_ACTIVITY = 987709;
    private UserResponse userData = new UserResponse();
    private FragmentProfileBinding binding;
    private int USER_ID = 0;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        loadingDialog = new LoadingDialog(requireActivity());
        loadingDialog.show();


        // Lấy giá trị từ SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        USER_ID = sharedPref.getInt("user_id", 0);
        if (USER_ID == 0) {
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        // Get user data from api
        UserApi userApi = ApiClient.getUserAPI();
        Call<UserApiResponse> callUser = userApi.getDetailUser(USER_ID);
        callUser.enqueue(new Callback<UserApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    // Check if user exists
                    if (response.body().getId() != 0) {
                        userData.setId(response.body().getId());
                        userData.setFirstName(response.body().getFirstname());
                        userData.setPhone(response.body().getPhone());
                        userData.setLastName(response.body().getLastname());
                        userData.setEducation(response.body().getEducation());
                        userData.setBirth(response.body().getBirth());
                        userData.setGender(response.body().getGender());
                        userData.setPhotoUrl(response.body().getPhotoUrl());
                        userData.setTimeStartingWorking(response.body().getTimeStartingWork());
                        userData.setEmail(response.body().getEmail());
                        userData.setCountry(response.body().getCountry());
                        userData.setCity(response.body().getCity());
                        userData.setProvince(response.body().getProvince());


                        binding.usernameTextview.setText(userData.getFirstName() + " " + userData.getLastName());
                        if (userData.getPhone() != null) {
                            binding.userPhone.setText(userData.getPhone());
                            binding.userPhone.setVisibility(View.VISIBLE);
                        } else {
                            binding.userPhone.setVisibility(View.GONE);
                        }

                        Glide.with(requireActivity())
                                .load(userData.getPhotoUrl())
                                .error(R.drawable.avatar_thinking_svgrepo_com)
                                .into(binding.userAvatar);

                        // Dismiss dialog when api call done
                        loadingDialog.cancel();
                    } else {
                        // Back to login screen if not user
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d("User_Data_Error", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("User_Data_Error", t.toString());
            }
        });

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng xuất
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //bắt sự kiện cho boxProfile
        binding.boxProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("user_data", userData);
                intent.putExtra("caller", "ProfileFragment");
                startActivityForResult(intent, REQUEST_CODE_TO_PROFILE_ACTIVITY);
            }
        });

        binding.boxJobCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JobCriteriaActivity.class);
                startActivity(intent);
            }
        });

        // button go to user profile
        binding.buttonEditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("user_data", userData);
                intent.putExtra("caller", "ProfileFragment");
                startActivityForResult(intent, REQUEST_CODE_TO_PROFILE_ACTIVITY);
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
            }

        });

        // button go to upload cv screen
        binding.btnUploadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadFileActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel homeViewModel =  new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getCurrentTotalBookmark().observe(getViewLifecycleOwner(), totalBookmark -> {
            binding.totalBookmark.setText(totalBookmark.toString());
        });

        homeViewModel.getCurrentTotalApplied().observe(getViewLifecycleOwner(), totalApplied -> {
            binding.totalApply.setText(totalApplied.toString());
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TO_PROFILE_ACTIVITY && resultCode == getActivity().RESULT_OK) {

            if (data != null) {
                UserResponse updatedUserData = (UserResponse) data.getSerializableExtra("data_user_updated");
                if (updatedUserData != null) {
                    userData = updatedUserData;
                    binding.usernameTextview.setText(userData.getFirstName() + " " + userData.getLastName());
                    if (userData.getPhone() != null) {
                        binding.userPhone.setText(userData.getPhone());
                        binding.userPhone.setVisibility(View.VISIBLE);
                    } else {
                        binding.userPhone.setVisibility(View.GONE);
                    }

                    Glide.with(requireActivity())
                            .load(userData.getPhotoUrl())
                            .error(R.drawable.avatar_thinking_svgrepo_com)
                            .into(binding.userAvatar);
                }
            }
        }
    }
}