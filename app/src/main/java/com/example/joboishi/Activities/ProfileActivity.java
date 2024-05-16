package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.ProfileRecyclerViewAdapter;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserResponse;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Models.InforProfileAdd;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.LoadingDialog;
import com.example.joboishi.databinding.ActivityProfileBinding;
import com.makeramen.roundedimageview.RoundedImageView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ProfileActivity extends AppCompatActivity {

    private final ArrayList<InforProfileAdd> profileRecyclerViewData = new ArrayList<>();
    private final ArrayList<ImageView> indicators = new ArrayList<>();
    private final int STATUS_NO_INTERNET = 0;
    private final int STATUS_LOW_INTERNET = 1;
    private final int STATUS_GOOD_INTERNET = 2;
    private final UserResponse userData = new UserResponse();
    private final int USER_ID = 1;
    private final int REQUEST_CODE_TO_EDIT_PROFILE_ACTIVITY = 9191;
    LinearLayout indicatorLayout;
    private int currentPosition = 0;
    private Intent profileIntent;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirst = true;
    private ActivityProfileBinding binding;
    private RoundedImageView userAvatar;
    private LoadingDialog loadingDialog;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Register receiver
        registerInternetBroadcastReceiver();

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show loading while fetching data
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        // Get view
        userAvatar = findViewById(R.id.user_avatar);
        TextView userName = findViewById(R.id.username_textview);
        TextView userLocation = findViewById(R.id.user_location_textview);
        TextView userBirth = findViewById(R.id.user_birth_textview);
        TextView userGender = findViewById(R.id.user_gender_textview);
        TextView userEdu = findViewById(R.id.user_edu_textview);
        TextView userExperience = findViewById(R.id.user_experience_textview);

        // Init data
        profileRecyclerViewData.add(new InforProfileAdd("Công việc gần nhất", "Thêm kinh nghiệm làm việc để nhà tuyển dụng thấy thành tựu và trách nhiệm mà bạn đạt được", "Thêm kinh nghiệm làm việc", ProfileRecyclerViewAdapter.BUILDING_ICON));
        profileRecyclerViewData.add(new InforProfileAdd("Trình độ học vấn của bạn", "Thêm trình độ học vấn để nhà tuyển dụng dễ dàng tìm thấy bạn thấy", "Thêm trình độ học vấn", ProfileRecyclerViewAdapter.EDU_ICON));
        profileRecyclerViewData.add(new InforProfileAdd("Trường học gần đây", "Thêm học vấn để nhà tuyển dụng cân nhắc dễ dàng hơn", "Thêm học vẫn", ProfileRecyclerViewAdapter.EDU_ICON));
        profileRecyclerViewData.add(new InforProfileAdd("Top kỹ năng của bạn", "Cho nhà tuyển dụng thấy khả năng của bạn như thế nào", "Thêm kĩ năng", ProfileRecyclerViewAdapter.SKILL_ICON));

        // Get recycler view
        RecyclerView recyclerView = findViewById(R.id.profile_recycler_view);
        ProfileRecyclerViewAdapter recyclerViewAdapter = new ProfileRecyclerViewAdapter(this, profileRecyclerViewData, recyclerView);
        // Get button navigate to edit profile page
        ImageButton btnNavigateToEditProfilePage = findViewById(R.id.button_navigate_edit_profile_screen);
        // Change title of toolbar
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.profile_toolbar_title);

        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Create indicator slide
        indicatorLayout = findViewById(R.id.indicator);

        for (int i = 0; i < profileRecyclerViewData.size(); i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(R.drawable.indicator);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(14, 14);
            layoutParams.setMargins(8, 0, 8, 0);
            indicator.setLayoutParams(layoutParams);
            indicatorLayout.addView(indicator);
            indicators.add(indicator);
        }

        // Scroll Smooth
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Update indicator when scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                updateIndicators(firstVisibleItemPosition);
            }
        });

        // Event handler for button
        // Button navigate to edit user profile screen
        btnNavigateToEditProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                profileIntent.putExtra("user_data", userData);
                startActivityForResult(profileIntent, REQUEST_CODE_TO_EDIT_PROFILE_ACTIVITY);
            }
        });

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Lister swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet) {
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                if (statusInternet == STATUS_NO_INTERNET) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Get user info throw api by user id
        // Get user data from api
        UserApi userApi = ApiClient.getUserAPI();
        Call<UserApiResponse> callUser = userApi.getDetailUser(USER_ID);
        callUser.enqueue(new Callback<UserApiResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    userData.setId(response.body().getId());
                    userData.setFirstName(response.body().getFirstname());
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

                    Log.d("avatar", userData.getPhotoUrl());

                    Glide.with(getBaseContext())
                            .load(userData.getPhotoUrl())
                            .error(R.drawable.avatar_thinking_svgrepo_com)
                            .into(userAvatar);

                    userName.setText(userData.getFirstName() + " " + userData.getLastName());


                    if (userData.getCity() != null) {
                        userLocation.setText(userData.getCity());
                    }

                    if (userData.getGender() != null) {
                        userGender.setText(userData.getGender());
                    }

                    if (userData.getEducation() != null) {
                        userEdu.setText(userData.getEducation());
                    }

                    if (userData.getBirth() != null) {
                        userBirth.setText(userData.getBirth());
                    }

                    if (userData.getTimeStartingWorking() != null) {
                        userExperience.setText(userData.getTimeStartingWorking());
                    }

                    // Dismiss dialog when api call done
                    loadingDialog.cancel();
                } else {
                    Log.d("User_Data_Error", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("User_Data_Error", t.toString());
            }
        });
    }

    // Update indicator
    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.size(); i++) {
            if (i == position) {
                indicators.get(i).setImageResource(R.drawable.indicator_active); // Active
            } else {
                indicators.get(i).setImageResource(R.drawable.indicator); // Not active
            }
        }
    }

    // Auto slide recycler view
    private void autoSlide(RecyclerView recyclerView) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentPosition++;
                if (currentPosition == profileRecyclerViewData.size()) {
                    currentPosition = 0;
                }

                recyclerView.smoothScrollToPosition(currentPosition);
                // handle loop
                handler.postDelayed(this, 4000);
            }
        });
    }

    private void registerInternetBroadcastReceiver() {
        InternetBroadcastReceiver internetBroadcastReceiver = new InternetBroadcastReceiver();
        internetBroadcastReceiver.listener = new InternetBroadcastReceiver.IInternetBroadcastReceiverListener() {
            @Override
            public void noInternet() {
                statusPreInternet = STATUS_NO_INTERNET;
                if (isFirst) {
                    binding.main.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                    binding.image.setAnimation(R.raw.a404);
                    binding.image.playAnimation();
                    statusInternet = STATUS_NO_INTERNET;
                    binding.swipeRefreshLayout.setRefreshing(false);
                    isFirst = false;

                }
                new AestheticDialog.Builder(ProfileActivity.this, DialogStyle.CONNECTIFY, DialogType.ERROR)
                        .setTitle("Không có kết nối mạng")
                        .setMessage("Vui lòng kiểm tra lại kết nối mạng")
                        .setCancelable(false)
                        .setGravity(Gravity.BOTTOM).show();
            }

            @Override
            public void lowInternet() {
                binding.image.setVisibility(View.VISIBLE);
                binding.main.setVisibility(View.GONE);
            }

            @Override
            public void goodInternet() {
                statusPreInternet = STATUS_GOOD_INTERNET;
                if (isFirst) {
                    statusInternet = STATUS_GOOD_INTERNET;
                    isFirst = false;
                } else {
                    binding.image.setVisibility(View.GONE);
                    binding.main.setVisibility(View.VISIBLE);
                    MotionToast.Companion.createToast(ProfileActivity.this, "😍",
                            "Kết nối mạng đã được khôi phục",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ProfileActivity.this, R.font.helvetica_regular));
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }
}