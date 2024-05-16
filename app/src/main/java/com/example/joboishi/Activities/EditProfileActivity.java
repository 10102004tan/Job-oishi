package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.CountryApi;
import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.Api.ProvinceApi;
import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserRequest;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.SelectBirthFragment;
import com.example.joboishi.Fragments.SelectCityFragment;
import com.example.joboishi.Fragments.SelectCountryFragment;
import com.example.joboishi.Fragments.SelectEducationFragment;
import com.example.joboishi.Fragments.SelectExperienceFragment;
import com.example.joboishi.Fragments.SelectGenderFragment;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.LoadingDialog;
import com.example.joboishi.databinding.ActivityEditProfileBinding;
import com.makeramen.roundedimageview.RoundedImageView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class EditProfileActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;
    private final ArrayList<CountryApiResponse> countryData = new ArrayList<>(); // storage list country in the world
    private final ArrayList<ProvinceApiResponse> provinceData = new ArrayList<>(); // storage list province of VN
    private final ArrayList<String> genderData = new ArrayList<>(Arrays.asList("Nam", "Nữ", "Khác")); // List gender data
    private final ArrayList<String> EDUCATIONS = new ArrayList<>(Arrays.asList("Tiểu học", "Trung Học Cơ Sở", "Trung Học Phổ Thông", "Bảng Liên Kết", "Cao Đẳng", "Cử Nhân", "Thạc Sĩ", "Tiến Sĩ")); // List edu data
    private final ArrayList<String> EXPERIENCES = new ArrayList<>(Arrays.asList("Tôi đã có kinh nghiệm", "Tôi chưa có kinh nghiệm")); // List experience data
    private final Date currentDate = new Date();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final String formattedDate = sdf.format(currentDate);
    private final UserApiResponse userData = new UserApiResponse();
    private final ArrayList<CountryApiResponse> tempCountry = new ArrayList<>();
    private final ArrayList<ProvinceApiResponse> tempCity = new ArrayList<>();
    private final SelectCountryFragment countryFragment = new SelectCountryFragment();
    private final SelectCityFragment cityFragment = new SelectCityFragment();
    private final SelectBirthFragment birthFragment = new SelectBirthFragment();
    private final SelectGenderFragment genderFragment = new SelectGenderFragment();
    private final SelectEducationFragment eduFragment = new SelectEducationFragment();
    private final SelectExperienceFragment experienceFragment = new SelectExperienceFragment();
    private final int STATUS_NO_INTERNET = 0;
    private final int STATUS_LOW_INTERNET = 1;
    private final int STATUS_GOOD_INTERNET = 2;
    private final String DEFAULT_COUNTRY_STR = "Chọn quốc gia";
    private final String DEFAULT_CITY_STR = "Chọn thành phố";
    private final String DEFAULT_BIRTH_STR = "Chọn ngày tháng năm sinh của bạn";
    private final String DEFAULT_EDU_STR = "Chọn trình độ học vấn của bạn";
    private final String DEFAULT_GENDER_STR = "Chọn giới tính của bạn";
    private final String DEFAULT_NO_EXPERIENCE_STR = "Tôi chưa có kinh nghiệm";
    private final int USER_ID = 1;
    private final String experienceStartedDateValue = "04/2022"; // Begin employment time
    private final String experienceSelectedValue = "Tôi đã có kinh nghiệm"; // Hard experience of user
    private final String eduSelectedValue = "Cao Đẳng"; // Hard edu of user
    private final String countrySelectedValue = "Vietnam"; // Hard Nationality of user
    private final String citySelectedValue = "Thành phố Hồ Chí Minh"; // Hard City of user
    private final String genderSelectedValue = "Nam"; // Hard gender of user
    private final String tempExperience = "";
    private final boolean isHaveExperience = true; // experience of user
    private final String birthDate = "24/04/2004"; // birth date of user
    private TextView countryTextView; // use to display country of user
    private TextView cityTextView; // use to display city of user
    private TextView genderTextView; // use to display gender of user
    private TextView eduTextView; // use to display edu of user
    private TextView experienceTextView; // use to display experience of user
    private TextView birthDayTextView; // use to display birth date of user
    private int dayOfBirth;
    private int monthOfBirth;
    private int yearOfBirth;
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private InternetBroadcastReceiver internetBroadcastReceiver;
    private IntentFilter intentFilter;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirst = true;
    private ActivityEditProfileBinding binding;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private RoundedImageView userAvatar;
    private LoadingDialog loadingDialog;
    private TextView errorFirstnameTextView;
    private TextView errorLastnameTextView;

    // Func to check if a string contains char
    // Ex: "Vietnam" will contains "vi" , "am" , "nam" , "iet"
    // Pattern like *."char".*
    public static boolean checkPattern(String inputString, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inputString);
        return m.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        //register broadcast receiver
        registerInternetBroadcastReceiver();

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show dialog loading when fetching data
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();


        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.edit_profile_toolbar_title);

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set default value for EditText
        countryTextView = findViewById(R.id.selected_country_label);
        // use to display country of user

        // Get user avatar view
        userAvatar = findViewById(R.id.user_avatar);

        // Error firstname textview
        errorFirstnameTextView = findViewById(R.id.error_firstname_textview);
        // Error lastname textview
        errorLastnameTextView = findViewById(R.id.error_lastname_textview);


        countryTextView = findViewById(R.id.selected_country_label);
        firstNameTextView = findViewById(R.id.firstname_textview);
        lastNameTextView = findViewById(R.id.lastname_textview);
        // Handle when firstname text view change
        firstNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    errorFirstnameTextView.setVisibility(View.VISIBLE);
                } else {
                    errorFirstnameTextView.setVisibility(View.GONE);
                }
            }
        });


        lastNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    errorLastnameTextView.setVisibility(View.VISIBLE);
                } else {
                    errorLastnameTextView.setVisibility(View.GONE);
                }
            }
        });


        // Hard Nationality of user
        String countrySelectedValue = DEFAULT_COUNTRY_STR;
        countryTextView.setText(countrySelectedValue);
        countryFragment.setCountryTextView(countryTextView);
        countryFragment.setCountrySelectedValue(countrySelectedValue);
        countryFragment.setCountryData(countryData);

        cityTextView = findViewById(R.id.selected_city_label);
        cityTextView.setText(citySelectedValue);
        cityFragment.setCityData(provinceData);
        cityFragment.setCityTextView(cityTextView);
        cityFragment.setCitySelectedValue(citySelectedValue);

        genderTextView = findViewById(R.id.selected_gender);
        genderTextView.setText(genderSelectedValue);
        genderFragment.setGenderTextView(genderTextView);
        genderFragment.setGenderData(genderData);
        genderFragment.setGenderSelectedValue(genderSelectedValue);

        eduTextView = findViewById(R.id.selected_edu);
        eduTextView.setText(eduSelectedValue);
        eduFragment.setEduTextView(eduTextView);
        eduFragment.setEduData(EDUCATIONS);
        eduFragment.setEduSelectedValue(eduSelectedValue);

        experienceTextView = findViewById(R.id.selected_experience);
        experienceTextView.setText(experienceSelectedValue);
        experienceFragment.setExperienceSelectedValue(experienceSelectedValue);
        experienceFragment.setHaveExperience(isHaveExperience);
        experienceFragment.setExperienceData(EXPERIENCES);
        experienceFragment.setExperienceTextView(experienceTextView);
        experienceFragment.setExperienceStartedDateValue(experienceStartedDateValue);

        birthDayTextView = findViewById(R.id.selected_birth);
        birthDayTextView.setText(birthDate);
        birthFragment.setBirthDate(birthDate);
        birthFragment.setBirthDayTextView(birthDayTextView);


        //lister swipe refresh layout
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

        // Update user
        Button btnUpdateUser = findViewById(R.id.btn_update_user);
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

        // Update avatar user
        ImageView btnUpdateAvatarUser = findViewById(R.id.update_avatar_user);
        btnUpdateAvatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Choose user picture here
                openImagePicker();
            }
        });

        // Get province data from api
        ProvinceApi provinceApi = ApiClient.getProvinceAPI();
        Call<ArrayList<ProvinceApiResponse>> callProvince = provinceApi.getData();
        callProvince.enqueue(new Callback<ArrayList<ProvinceApiResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ProvinceApiResponse>> call, @NonNull Response<ArrayList<ProvinceApiResponse>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    provinceData.addAll(response.body());
                    tempCity.addAll(response.body());
                    cityFragment.setCityData(provinceData);
                    cityFragment.setTemp(tempCity);
                } else {
                    Log.d("PROVINCE_API_ERROR", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ProvinceApiResponse>> call, @NonNull Throwable t) {
                Log.d("PROVINCE_API_ERROR", t.toString());
            }
        });

        // Get country data from api
        CountryApi countryApi = ApiClient.getCountryAPI();
        Call<ArrayList<CountryApiResponse>> callCountry = countryApi.getData();
        callCountry.enqueue(new Callback<ArrayList<CountryApiResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CountryApiResponse>> call, @NonNull Response<ArrayList<CountryApiResponse>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    countryData.addAll(response.body());
                    tempCountry.addAll(response.body());
                    countryFragment.setCountryData(countryData);
                    countryFragment.setTemp(tempCountry);
                } else {
                    Log.d("COUNTRY_API_ERROR", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CountryApiResponse>> call, @NonNull Throwable t) {
                Log.d("COUNTRY_API_ERROR", t.toString());
            }
        });

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
                    userData.setFirstname(response.body().getFirstname());
                    userData.setLastname(response.body().getLastname());
                    userData.setEducation(response.body().getEducation());
                    userData.setBirth(response.body().getBirth());
                    userData.setGender(response.body().getGender());
                    userData.setPhotoUrl(response.body().getPhotoUrl());
                    userData.setTimeStartingWork(response.body().getTimeStartingWork());
                    userData.setEmail(response.body().getEmail());
                    userData.setCountry(response.body().getCountry());
                    userData.setCity(response.body().getCity());
                    userData.setProvince(response.body().getProvince());

                    Log.d("avatar", userData.getPhotoUrl());

                    Glide.with(getBaseContext())
                            .load(userData.getPhotoUrl())
                            .error(R.drawable.avatar_thinking_svgrepo_com)
                            .into(userAvatar);

                    firstNameTextView.setText(userData.getFirstname());
                    lastNameTextView.setText(userData.getLastname());

                    if (userData.getCountry() != null) {
                        countryFragment.setCountrySelectedValue(userData.getCountry());
                        countryTextView.setText(userData.getCountry());
                    }

                    if (userData.getCity() != null) {
                        cityFragment.setCitySelectedValue(userData.getCity());
                        cityTextView.setText(userData.getCity());
                    }

                    if (userData.getGender() != null) {
                        genderFragment.setGenderSelectedValue(userData.getGender());
                        genderTextView.setText(userData.getGender());
                    }

                    if (userData.getEducation() != null) {
                        eduFragment.setEduSelectedValue(userData.getEducation());
                        eduTextView.setText(userData.getEducation());
                    }

                    if (userData.getBirth() != null) {
                        birthFragment.setBirthDate(userData.getBirth());
                        birthDayTextView.setText(userData.getBirth());
                    }

                    if (userData.getTimeStartingWork() == null) {
                        experienceFragment.setHaveExperience(false);
                    } else {
                        experienceTextView.setText(userData.getTimeStartingWork());
                        experienceFragment.setHaveExperience(true);
                        experienceFragment.setExperienceStartedDateValue(userData.getTimeStartingWork());
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


        // Show bottom sheet dialog to choose country
        LinearLayout btnShowCountryBottomSheet = findViewById(R.id.btn_country_spinner);
        btnShowCountryBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountryDialog();
            }
        });

        // Show bottom sheet dialog to choose province
        LinearLayout btnShowProvinceBottomSheet = findViewById(R.id.btn_city_spinner);
        btnShowProvinceBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityDialog();
            }
        });

        // Choose gender
        LinearLayout btnChooseGender = findViewById(R.id.btn_choose_gender);
        btnChooseGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });

        // Choose edu
        LinearLayout btnChooseEdu = findViewById(R.id.btn_choose_edu);
        btnChooseEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEduDialog();
            }
        });

        // Choose experience
        LinearLayout btnChooseExperience = findViewById(R.id.btn_choose_experience);
        btnChooseExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExperienceDialog();
            }
        });

        // Choose birth
        LinearLayout btnChooseBirth = findViewById(R.id.btn_choose_birth);
        btnChooseBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBirthDialog();
            }
        });

    }

    // Dialog country
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showCountryDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(countryFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");

    }

    // Dialog city
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showCityDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(cityFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");
    }

    // Dialog gender
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showGenderDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(genderFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");
    }

    // Dialog education
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showEduDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(eduFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");
    }

    // Dialog experience
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showExperienceDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(experienceFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");
    }

    // Dialog birth
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showBirthDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(birthFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");

    }


    private void registerInternetBroadcastReceiver() {
        internetBroadcastReceiver = new InternetBroadcastReceiver();
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
                new AestheticDialog.Builder(EditProfileActivity.this, DialogStyle.CONNECTIFY, DialogType.ERROR)
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
                    MotionToast.Companion.createToast(EditProfileActivity.this, "😍",
                            "Kết nối mạng đã được khôi phục",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EditProfileActivity.this, R.font.helvetica_regular));
                }
            }
        };
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    // Update user information
    public void updateUserInfo() {

        String firstName = firstNameTextView.getText().toString();
        String lastName = lastNameTextView.getText().toString();
        String country = countryTextView.getText().toString();
        String city = cityTextView.getText().toString();
        String birth = birthDayTextView.getText().toString();
        String gender = genderTextView.getText().toString();
        String edu = eduTextView.getText().toString();
        String timeStartingWork = experienceTextView.getText().toString();

        if (country.equals(DEFAULT_COUNTRY_STR)) {
            country = null;
        }

        if (city.equals(DEFAULT_CITY_STR)) {
            city = null;
        }

        if (birth.equals(DEFAULT_BIRTH_STR)) {
            birth = null;
        }

        if (gender.equals(DEFAULT_GENDER_STR)) {
            gender = null;
        }

        if (edu.equals(DEFAULT_EDU_STR)) {
            edu = null;
        }

        if (timeStartingWork.equals(DEFAULT_NO_EXPERIENCE_STR)) {
            timeStartingWork = null;
        }

        UserRequest userUpdateRequest = new UserRequest();
        userUpdateRequest.setId(userData.getId());
        userUpdateRequest.setFirst_name(firstName);
        userUpdateRequest.setLast_name(lastName);
        userUpdateRequest.setEducation(edu);
        userUpdateRequest.setBirth(birth);
        userUpdateRequest.setGender(gender);
        userUpdateRequest.setPhoto_url(userData.getPhotoUrl()); // Note: handler later
        userUpdateRequest.setTime_starting_work(timeStartingWork);
        userUpdateRequest.setEmail(userData.getEmail());
        userUpdateRequest.setCountry(country);
        userUpdateRequest.setCity(city);
        userUpdateRequest.setProvince(userData.getProvince()); // Note: handler later


        UserApi userApi = ApiClient.getUserAPI();
        Call<UserApiResponse> call = userApi.updateUserInfo(USER_ID, userUpdateRequest);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    userData.setId(response.body().getId());
                    userData.setFirstname(response.body().getFirstname());
                    userData.setLastname(response.body().getLastname());
                    userData.setEducation(response.body().getEducation());
                    userData.setBirth(response.body().getBirth());
                    userData.setGender(response.body().getGender());
                    userData.setPhotoUrl(response.body().getPhotoUrl());
                    userData.setTimeStartingWork(response.body().getTimeStartingWork());
                    userData.setEmail(response.body().getEmail());
                    userData.setCountry(response.body().getCountry());
                    userData.setCity(response.body().getCity());
                    userData.setProvince(response.body().getProvince());

                    firstNameTextView.setText(userData.getFirstname());
                    lastNameTextView.setText(userData.getLastname());

                    if (userData.getCountry() == null) {
                        countryTextView.setText(R.string.choose_contry_textview);
                    } else {
                        countryTextView.setText(userData.getCountry());
                    }

                    if (userData.getCity() == null) {
                        cityTextView.setText(R.string.choose_city_textview);
                    } else {
                        cityTextView.setText(userData.getCity());
                    }

                    if (userData.getGender() == null) {
                        genderTextView.setText(R.string.choose_gender_textview);
                    } else {
                        genderTextView.setText(userData.getGender());
                    }

                    if (userData.getEducation() == null) {
                        eduTextView.setText(R.string.choose_edu_textview);
                    } else {
                        eduTextView.setText(userData.getEducation());
                    }

                    if (userData.getBirth() == null) {
                        birthDayTextView.setText(R.string.choose_birth_textview);
                    } else {
                        birthDayTextView.setText(userData.getBirth());
                    }

                    if (userData.getTimeStartingWork() == null) {
                        experienceTextView.setText(R.string.not_experience);
                        experienceFragment.setHaveExperience(false);
                    } else {
                        experienceTextView.setText(userData.getTimeStartingWork());
                        experienceFragment.setHaveExperience(true);
                        experienceFragment.setExperienceStartedDateValue(userData.getTimeStartingWork());
                    }


                    firstNameTextView.setText(userData.getFirstname());
                    firstNameTextView.setText(userData.getFirstname());

                    finish();

                } else {
                    Log.d("UPDATE_USER_ERROR", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("UPDATE_USER_ERROR", t.toString());
            }
        });
    }

    // Resize image size
    public Bitmap getResizedBitmapWithPadding(Bitmap image, int maxWidth, int maxHeight) {
        float ratioBitmap = (float) image.getWidth() / (float) image.getHeight();
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;
        if (ratioMax > 1) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);

        Bitmap outputBitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(Color.TRANSPARENT);

        int left = (maxWidth - finalWidth) / 2;
        int top = (maxHeight - finalHeight) / 2;
        canvas.drawBitmap(resizedBitmap, left, top, null);

        return outputBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap resizedBitmap = getResizedBitmapWithPadding(bitmap, 300, 300);
                userAvatar.setImageBitmap(resizedBitmap);
                uploadImage(resizedBitmap, String.valueOf(USER_ID));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Bitmap bitmap, String userId) {
        File file = bitmapToFile(bitmap, userId + "avatar.jpg");
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo_url", file.getName(), requestFile);

            UserApi userApi = ApiClient.getUserAPI();
            Call<UserApiResponse> call = userApi.updateAvatar(USER_ID, body);
            call.enqueue(new Callback<UserApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                    Toast.makeText(EditProfileActivity.this, "Ảnh đại diện của bạn đã được cập nhật", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật ảnh", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, fileName);

        OutputStream os;
        try {
            os = Files.newOutputStream(imageFile.toPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return imageFile;
    }

    // Open Image Picker to choose picture
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}