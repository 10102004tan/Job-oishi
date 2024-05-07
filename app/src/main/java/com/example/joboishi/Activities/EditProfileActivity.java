package com.example.joboishi.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.Adapters.CityRecyclerViewAdapter;
import com.example.joboishi.Adapters.SimpleStringRecyclerViewAdapter;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.CountryApi;
import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.Api.ProvinceApi;
import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserRequest;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.Fragments.SelectBirthFragment;
import com.example.joboishi.Fragments.SelectCityFragment;
import com.example.joboishi.Fragments.SelectCountryFragment;
import com.example.joboishi.Fragments.SelectEducationFragment;
import com.example.joboishi.Fragments.SelectExperienceFragment;
import com.example.joboishi.Fragments.SelectGenderFragment;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.LoadingDialog;
import com.example.joboishi.Views.TimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermissionActivity;
import com.gun0912.tedpermission.normal.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {


    private final ArrayList<CountryApiResponse> countryData = new ArrayList<>(); // storage list country in the world
    private final ArrayList<ProvinceApiResponse> provinceData = new ArrayList<>(); // storage list province of VN
    private final ArrayList<String> genderData = new ArrayList<>(Arrays.asList("Nam", "Nữ", "Khác")); // List gender data
    private final ArrayList<String> EDUCATIONS = new ArrayList<>(Arrays.asList("Tiểu học", "Trung Học Cơ Sở", "Trung Học Phổ Thông", "Bảng Liên Kết", "Cao Đẳng", "Cử Nhân", "Thạc Sĩ", "Tiến Sĩ")); // List edu data
    private final ArrayList<String> EXPERIENCES = new ArrayList<>(Arrays.asList("Tôi đã có kinh nghiệm", "Tôi chưa có kinh nghiệm")); // List experience data
    private final UserApiResponse userData = new UserApiResponse();
    private TextView birthDayTextView;
    private TextView cityTextView;
    private TextView eduTextView;
    private TextView experienceTextView;
    private TextView countryTextView;
    private TextView genderTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private final ArrayList<CountryApiResponse> tempCountry = new ArrayList<>();
    private final ArrayList<ProvinceApiResponse> tempCity = new ArrayList<>();
    private final SelectCountryFragment countryFragment = new SelectCountryFragment();
    private final SelectCityFragment cityFragment = new SelectCityFragment();
    private final SelectBirthFragment birthFragment = new SelectBirthFragment();
    private final SelectGenderFragment genderFragment = new SelectGenderFragment();
    private final SelectEducationFragment eduFragment = new SelectEducationFragment();
    private final SelectExperienceFragment experienceFragment = new SelectExperienceFragment();
    private final String DEFAULT_COUNTRY_STR = "Chọn quốc gia";
    private final String DEFAULT_CITY_STR = "Chọn thành phố";
    private final String DEFAULT_BIRTH_STR = "Chọn ngày tháng năm sinh của bạn";
    private final String DEFAULT_EDU_STR = "Chọn trình độ học vấn của bạn";
    private final String DEFAULT_GENDER_STR = "Chọn giới tính của bạn";
    private final String DEFAULT_NO_EXPERIENCE_STR = "Tôi chưa có kinh nghiệm";
    private final int USER_ID = 7;
    private static final int PICK_IMAGE_REQUEST = 1;
    private LoadingDialog loadingDialog;






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
        setContentView(com.example.joboishi.R.layout.activity_edit_profile);
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
        // use to display country of user
        countryTextView = findViewById(R.id.selected_country_label);
        firstNameTextView = findViewById(R.id.firstname_textview);
        lastNameTextView = findViewById(R.id.lastname_textview);

        // Hard Nationality of user
        String countrySelectedValue = DEFAULT_COUNTRY_STR;
        countryTextView.setText(countrySelectedValue);
        countryFragment.setCountryTextView(countryTextView);
        countryFragment.setCountrySelectedValue(countrySelectedValue);
        countryFragment.setCountryData(countryData);

        // use to display city of user
        cityTextView = findViewById(R.id.selected_city_label);
        // Hard City of user
        String citySelectedValue = DEFAULT_CITY_STR;
        cityTextView.setText(citySelectedValue);
        cityFragment.setCityData(provinceData);
        cityFragment.setCityTextView(cityTextView);
        cityFragment.setCitySelectedValue(citySelectedValue);

        // use to display gender of user
        genderTextView = findViewById(R.id.selected_gender);
        // Hard gender of user
        String genderSelectedValue = DEFAULT_GENDER_STR;
        genderTextView.setText(genderSelectedValue);
        genderFragment.setGenderTextView(genderTextView);
        genderFragment.setGenderData(genderData);
        genderFragment.setGenderSelectedValue(genderSelectedValue);

        // use to display edu of user
        eduTextView = findViewById(R.id.selected_edu);
        // Hard edu of user
        String eduSelectedValue = DEFAULT_EDU_STR;
        eduTextView.setText(eduSelectedValue);
        eduFragment.setEduTextView(eduTextView);
        eduFragment.setEduData(EDUCATIONS);
        eduFragment.setEduSelectedValue(eduSelectedValue);

        // use to display experience of user
        experienceTextView = findViewById(R.id.selected_experience);
        // Hard experience of user
        String experienceSelectedValue = DEFAULT_NO_EXPERIENCE_STR;
        experienceTextView.setText(experienceSelectedValue);
        experienceFragment.setExperienceSelectedValue(experienceSelectedValue);
        // experience of user
        boolean isHaveExperience = false;
        experienceFragment.setHaveExperience(isHaveExperience);
        experienceFragment.setExperienceData(EXPERIENCES);
        experienceFragment.setExperienceTextView(experienceTextView);
        // Begin employment time
        String experienceStartedDateValue = "04/2024";
        experienceFragment.setExperienceStartedDateValue(experienceStartedDateValue);

        // use to display birth date of user
        birthDayTextView = findViewById(R.id.selected_birth);
        // birth date of user
        String birthDate = "24/04/2004";
        birthDayTextView.setText(DEFAULT_BIRTH_STR);
        birthFragment.setBirthDate(birthDate);
        birthFragment.setBirthDayTextView(birthDayTextView);


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
                    Log.d("user_test", response.body().getEmail());

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

                    if (userData.getCountry() != null){
                        countryFragment.setCountrySelectedValue(userData.getCountry());
                        countryTextView.setText(userData.getCountry());
                    }

                    if (userData.getCity() != null){
                        cityFragment.setCitySelectedValue(userData.getCity());
                        cityTextView.setText(userData.getCity());
                    }

                    if (userData.getGender() != null){
                        genderFragment.setGenderSelectedValue(userData.getGender());
                        genderTextView.setText(userData.getGender());
                    }

                    if (userData.getEducation() != null){
                        eduFragment.setEduSelectedValue(userData.getEducation());
                        eduTextView.setText(userData.getEducation());
                    }

                    if (userData.getBirth() != null){
                        birthFragment.setBirthDate(userData.getBirth());
                        birthDayTextView.setText(userData.getBirth());
                    }

                    if (userData.getTimeStartingWork() == null){
                        experienceFragment.setHaveExperience(false);
                    }else {
                        experienceTextView.setText(userData.getTimeStartingWork());
                        experienceFragment.setHaveExperience(true);
                        experienceFragment.setExperienceStartedDateValue(userData.getTimeStartingWork());
                    }

                    // Dismiss dialog when api call done
                    loadingDialog.cancel();
                }else {
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

    // Open Image Picker to choose picture
    private void openImagePicker() {
    }

    // Dialog country
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showCountryDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(countryFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");

    }

    // Dialog city
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showCityDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(cityFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");
    }

    // Dialog gender
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showGenderDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(genderFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");
    }

    // Dialog education
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showEduDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(eduFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");
    }

    // Dialog experience
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showExperienceDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(experienceFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");
    }

    // Dialog birth
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showBirthDialog() {
        myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
        myBottomSheetDialogFragment.setFragment(birthFragment);
        myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");

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

                    if (userData.getCountry() == null){
                        countryTextView.setText(R.string.choose_contry_textview);
                    }else {
                        countryTextView.setText(userData.getCountry());
                    }

                    if (userData.getCity() == null){
                        cityTextView.setText(R.string.choose_city_textview);
                    }else {
                        cityTextView.setText(userData.getCity());
                    }

                    if (userData.getGender() == null){
                        genderTextView.setText(R.string.choose_gender_textview);
                    }else {
                        genderTextView.setText(userData.getGender());
                    }

                    if (userData.getEducation() == null){
                        eduTextView.setText(R.string.choose_edu_textview);
                    }else {
                        eduTextView.setText(userData.getEducation());
                    }

                    if (userData.getBirth() == null){
                        birthDayTextView.setText(R.string.choose_birth_textview);
                    }else {
                        birthDayTextView.setText(userData.getBirth());
                    }

                    if (userData.getTimeStartingWork() == null){
                        experienceTextView.setText(R.string.not_experience);
                        experienceFragment.setHaveExperience(false);
                    }else {
                        experienceTextView.setText(userData.getTimeStartingWork());
                        experienceFragment.setHaveExperience(true);
                        experienceFragment.setExperienceStartedDateValue(userData.getTimeStartingWork());
                    }


                    firstNameTextView.setText(userData.getFirstname());
                    firstNameTextView.setText(userData.getFirstname());

                    finish();

                }else {
                    Log.d("UPDATE_USER_ERROR", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("UPDATE_USER_ERROR", t.toString());
            }
        });

    }
}