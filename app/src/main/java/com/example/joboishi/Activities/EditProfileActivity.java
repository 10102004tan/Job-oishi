package com.example.joboishi.Activities;

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
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.Fragments.SelectBirthFragment;
import com.example.joboishi.Fragments.SelectCityFragment;
import com.example.joboishi.Fragments.SelectCountryFragment;
import com.example.joboishi.Fragments.SelectEducationFragment;
import com.example.joboishi.Fragments.SelectExperienceFragment;
import com.example.joboishi.Fragments.SelectGenderFragment;
import com.example.joboishi.R;
import com.example.joboishi.Views.TimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private String experienceStartedDateValue = "04/2022"; // Begin employment time


    private String experienceSelectedValue = "Tôi đã có kinh nghiệm"; // Hard experience of user
    private String eduSelectedValue = "Cao Đẳng"; // Hard edu of user
    private String countrySelectedValue = "Vietnam"; // Hard Nationality of user
    private String citySelectedValue = "Thành phố Hồ Chí Minh"; // Hard City of user
    private String genderSelectedValue = "Nam"; // Hard gender of user
    private TextView countryTextView; // use to display country of user
    private TextView cityTextView; // use to display city of user
    private TextView genderTextView; // use to display gender of user
    private TextView eduTextView; // use to display edu of user
    private TextView experienceTextView; // use to display experience of user
    private TextView birthDayTextView; // use to display birth date of user
    private String tempExperience = "";
    private boolean isHaveExperience = true; // experience of user
    private final Date currentDate = new Date();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final String formattedDate = sdf.format(currentDate);
    private int dayOfBirth;
    private int monthOfBirth;
    private int yearOfBirth;
    private String birthDate = "24/04/2004"; // birth date of user


    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private final ArrayList<CountryApiResponse> tempCountry = new ArrayList<>();
    private final ArrayList<ProvinceApiResponse> tempCity = new ArrayList<>();

    private final SelectCountryFragment countryFragment = new SelectCountryFragment();
    private final SelectCityFragment cityFragment = new SelectCityFragment();
    private final SelectBirthFragment birthFragment = new SelectBirthFragment();
    private final SelectGenderFragment genderFragment = new SelectGenderFragment();
    private final SelectEducationFragment eduFragment = new SelectEducationFragment();
    private final SelectExperienceFragment experienceFragment = new SelectExperienceFragment();


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
}