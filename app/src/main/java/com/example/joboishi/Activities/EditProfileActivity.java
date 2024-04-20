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


import com.datto.demo_android.api.CountryApiResponse;
import com.datto.demo_android.api.ProvinceApiResponse;
import com.example.joboishi.Adapters.CityRecyclerViewAdapter;
import com.example.joboishi.Adapters.CountryRecyclerViewAdapter;
import com.example.joboishi.Adapters.SimpleStringRecyclerViewAdapter;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.CountryApi;
import com.example.joboishi.Api.ProvinceApi;
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
    private final ArrayList<String> GENDERS = new ArrayList<>(Arrays.asList("Nam", "Nữ", "Khác")); // List gender data
    private final ArrayList<String> EDUCATIONS = new ArrayList<>(Arrays.asList("Tiểu học", "Trung Học Cơ Sở", "Trung Học Phổ Thông", "Bảng Liên Kết", "Cao Đẳng", "Cử Nhân", "Thạc Sĩ", "Tiến Sĩ")); // List edu data
    private final ArrayList<String> EXPERIENCES = new ArrayList<>(Arrays.asList("Tôi đã có kinh nghiệm", "Tôi chưa có kinh nghiệm")); // List experience data
    private final Date currentDate = new Date();
    // Định dạng ngày tháng
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    // Chuyển đổi thành chuỗi
    private final String formattedDate = sdf.format(currentDate);
    private String experienceStartedDateValue = "04/2022"; // Begin employment time
    private String birthDate = "24/04/2004"; // birth date of user
    private String experienceSelectedValue = "Tôi đã có kinh nghiệm"; // Hard experience of user
    private String eduSelectedValue = "Cao Đẳng"; // Hard edu of user
    private String searchingCityValue = ""; // use to search city name
    private String countrySelectedValue = "Vietnam"; // Hard Nationality of user
    private String citySelectedValue = "Thành phố Hồ Chí Minh"; // Hard City of user
    private String genderSelectedValue = "Nam"; // Hard gender of user
    private TextView countryTextView; // use to display country of user
    private TextView cityTextView; // use to display city of user
    private TextView genderTextView; // use to display gender of user
    private TextView eduTextView; // use to display edu of user
    private TextView experienceTextView; // use to display experience of user
    private TextView birthDayTextView; // use to display birth date of user
    private String searchingValue = ""; // use to search country name
    private ArrayList<CountryApiResponse> temp;
    private ArrayList<ProvinceApiResponse> tempCity;
    private String tempExperience = "";
    private boolean isHaveExperience = true; // experience of user
    private int dayOfBirth;
    private int monthOfBirth;
    private int yearOfBirth;

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

        // Set default value for EditText
        countryTextView = findViewById(R.id.selected_country_label);
        countryTextView.setText(countrySelectedValue);

        cityTextView = findViewById(R.id.selected_city_label);
        cityTextView.setText(citySelectedValue);

        genderTextView = findViewById(R.id.selected_gender);
        genderTextView.setText(genderSelectedValue);

        eduTextView = findViewById(R.id.selected_edu);
        eduTextView.setText(eduSelectedValue);

        experienceTextView = findViewById(R.id.selected_experience);
        experienceTextView.setText(experienceSelectedValue);

        birthDayTextView = findViewById(R.id.selected_birth);
        birthDayTextView.setText(birthDate);


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

        // Get country data from api
        CountryApi countryApi = ApiClient.getCountryAPI();
        Call<ArrayList<CountryApiResponse>> callCountry = countryApi.getData();
        callCountry.enqueue(new Callback<ArrayList<CountryApiResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CountryApiResponse>> call, @NonNull Response<ArrayList<CountryApiResponse>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("COUNTRY_SIZE", response.body().size() + "");
                    countryData.addAll(response.body());
                    temp = new ArrayList<>(countryData);
                } else {
                    Log.d("COUNTRY_API_ERROR", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CountryApiResponse>> call, @NonNull Throwable t) {
                Log.d("COUNTRY_API_ERROR", t.toString());
            }
        });

        // Get province data from api
        ProvinceApi provinceApi = ApiClient.getProvinceAPI();
        Call<ArrayList<ProvinceApiResponse>> callProvince = provinceApi.getData();
        callProvince.enqueue(new Callback<ArrayList<ProvinceApiResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ProvinceApiResponse>> call, @NonNull Response<ArrayList<ProvinceApiResponse>> response) {
                Log.d("PROVINCE_SIZE", response.body() + "");
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("PROVINCE_SIZE", response.body().size() + "");
                    provinceData.addAll(response.body());
                    tempCity = new ArrayList<>(provinceData);
                } else {
                    Log.d("PROVINCE_API_ERROR", "ERROR");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ProvinceApiResponse>> call, @NonNull Throwable t) {
                Log.d("PROVINCE_API_ERROR", t.toString());
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
        final BottomSheetDialog dialogCountry = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_country_layout, null);
        dialogCountry.setContentView(view);
        dialogCountry.show();


        RecyclerView listCountryRyc = dialogCountry.findViewById(R.id.list_country_ryc);
        CountryRecyclerViewAdapter recyclerViewAdapter = new CountryRecyclerViewAdapter(this, countryData, listCountryRyc, countrySelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listCountryRyc != null;
        listCountryRyc.setLayoutManager(layoutManager);
        listCountryRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new CountryRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CountryRecyclerViewAdapter.MyHolder holder) {
                countrySelectedValue = countryData.get(holder.getPos()).getName().getCommon();
                countryTextView.setText(countrySelectedValue);
                countryData.clear();
                countryData.addAll(temp);
                searchingValue = "";
                dialogCountry.dismiss();
            }
        });

        // Input search country
        EditText searchCountryEdt = dialogCountry.findViewById(R.id.input_search_country);
        if (!searchingValue.isEmpty()) {
            assert searchCountryEdt != null;
            searchCountryEdt.setText(searchingValue);
        }
        assert searchCountryEdt != null;

        searchCountryEdt.addTextChangedListener(new TextWatcher() {
            final ArrayList<CountryApiResponse> searchData = new ArrayList<>();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingValue = s.toString();

                if (s.toString().isEmpty()) {
                    // If value search empty
                    // Render first list to view
                    countryData.clear();
                    countryData.addAll(temp);
                    recyclerViewAdapter.notifyDataSetChanged();
                } else {
                    // If value search not empty
                    for (CountryApiResponse item :
                            countryData) {
                        if (checkPattern(item.getName().getCommon().toLowerCase(), ".*" + searchingValue.toLowerCase() + ".*")) {
                            // TolowerCase search value and country value to compare
                            // If country value contains search value => add to new array
                            searchData.add(item);
                        }
                    }
                    // After filter
                    // If have results
                    if (!searchData.isEmpty()) {
                        // Update result to countryData array
                        countryData.clear();
                        countryData.addAll(searchData);
                        searchData.clear(); // Clear data in searchArray after search
                        recyclerViewAdapter.notifyDataSetChanged(); // Update to recycler view
                    }
                }
            }
        });

        // Close dialog
        ImageButton btnClose = dialogCountry.findViewById(R.id.btn_close_country_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCountry.dismiss();
            }
        });
    }

    // Dialog city
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showCityDialog() {
        final BottomSheetDialog dialogCity = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_city_layout, null);
        dialogCity.setContentView(view);
        dialogCity.show();


        RecyclerView listCityRyc = dialogCity.findViewById(R.id.list_city_ryc);
        CityRecyclerViewAdapter recyclerViewAdapter = new CityRecyclerViewAdapter(this, provinceData, listCityRyc, citySelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listCityRyc != null;
        listCityRyc.setLayoutManager(layoutManager);
        listCityRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new CityRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CityRecyclerViewAdapter.MyHolder holder) {
                citySelectedValue = provinceData.get(holder.getPos()).getProvinceName();
                cityTextView.setText(citySelectedValue);
                provinceData.clear();
                provinceData.addAll(tempCity);
                searchingCityValue = "";
                dialogCity.dismiss();
            }
        });

        // Input search city
        EditText searchCityEdt = dialogCity.findViewById(R.id.input_search_city);
        if (!searchingCityValue.isEmpty()) {
            assert searchCityEdt != null;
            searchCityEdt.setText(searchingCityValue);
        }
        assert searchCityEdt != null;

        searchCityEdt.addTextChangedListener(new TextWatcher() {
            final ArrayList<ProvinceApiResponse> searchData = new ArrayList<>();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingCityValue = s.toString();

                if (s.toString().isEmpty()) {
                    // If value search empty
                    // Render first list to view
                    provinceData.clear();
                    provinceData.addAll(tempCity);
                    recyclerViewAdapter.notifyDataSetChanged();
                } else {
                    // If value search not empty
                    for (ProvinceApiResponse item :
                            provinceData) {
                        if (checkPattern(item.getProvinceName().toLowerCase(), ".*" + searchingCityValue.toLowerCase() + ".*")) {
                            // TolowerCase search value and country value to compare
                            // If country value contains search value => add to new array
                            searchData.add(item);
                        }
                    }
                    // After filter
                    // If have results
                    if (!searchData.isEmpty()) {
                        // Update result to countryData array
                        provinceData.clear();
                        provinceData.addAll(searchData);
                        searchData.clear(); // Clear data in searchArray after search
                        recyclerViewAdapter.notifyDataSetChanged(); // Update to recycler view
                    }
                }
            }
        });

        // Close dialog
        ImageButton btnClose = dialogCity.findViewById(R.id.btn_close_city_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCity.dismiss();
            }
        });
    }

    // Dialog gender
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showGenderDialog() {
        final BottomSheetDialog dialogGender = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_gender_layout, null);
        dialogGender.setContentView(view);
        dialogGender.show();


        RecyclerView listGenderRyc = dialogGender.findViewById(R.id.list_gender_ryc);
        SimpleStringRecyclerViewAdapter recyclerViewAdapter = new SimpleStringRecyclerViewAdapter(this, GENDERS, listGenderRyc, genderSelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listGenderRyc != null;
        listGenderRyc.setLayoutManager(layoutManager);
        listGenderRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {
                genderSelectedValue = GENDERS.get(holder.getPos());
                genderTextView.setText(genderSelectedValue);
                dialogGender.dismiss();
            }
        });

        // Close dialog
        ImageButton btnClose = dialogGender.findViewById(R.id.btn_close_gender_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGender.dismiss();
            }
        });
    }

    // Dialog education
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showEduDialog() {
        final BottomSheetDialog dialogEdu = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_edu_layout, null);
        dialogEdu.setContentView(view);
        dialogEdu.show();


        RecyclerView listEduRyc = dialogEdu.findViewById(R.id.list_edu_ryc);
        SimpleStringRecyclerViewAdapter recyclerViewAdapter = new SimpleStringRecyclerViewAdapter(this, EDUCATIONS, listEduRyc, eduSelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listEduRyc != null;
        listEduRyc.setLayoutManager(layoutManager);
        listEduRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {
                eduSelectedValue = EDUCATIONS.get(holder.getPos());
                eduTextView.setText(eduSelectedValue);
                dialogEdu.dismiss();
            }
        });

        // Close dialog
        ImageButton btnClose = dialogEdu.findViewById(R.id.btn_close_edu_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdu.dismiss();
            }
        });
    }

    // Dialog experience
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showExperienceDialog() {
        final BottomSheetDialog dialogExperience = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_experience_layout, null);
        dialogExperience.setContentView(view);
        dialogExperience.show();

        RecyclerView ListExperienceRyc = dialogExperience.findViewById(R.id.list_experience_ryc);
        SimpleStringRecyclerViewAdapter recyclerViewAdapter = new SimpleStringRecyclerViewAdapter(this, EXPERIENCES, ListExperienceRyc, experienceSelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert ListExperienceRyc != null;
        ListExperienceRyc.setLayoutManager(layoutManager);
        ListExperienceRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        TimePicker monthPicker = dialogExperience.findViewById(R.id.month_picker_start);
        TimePicker yearPicker = dialogExperience.findViewById(R.id.year_picker_end);

        int currentMonth = Integer.parseInt(formattedDate.split("/")[1]);
        int currentYear = Integer.parseInt(formattedDate.split("/")[2]);

        String messageExprience = "";
        TextView experienceMessageTextView = dialogExperience.findViewById(R.id.experience_year_message);


        // Choose time experience
        assert monthPicker != null;
        monthPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int month = Integer.parseInt(String.valueOf(view.getValue()));
            }
        });


        assert yearPicker != null;
        yearPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });


        // Show section time experience if user have experience
        LinearLayout haveExperience = dialogExperience.findViewById(R.id.have_experience);
        tempExperience = EXPERIENCES.get(1);
        if (isHaveExperience) {
            tempExperience = EXPERIENCES.get(0);
            assert haveExperience != null;
            haveExperience.setVisibility(View.VISIBLE);
            int month = Integer.parseInt(experienceStartedDateValue.split("/")[0]);
            int year = Integer.parseInt(experienceStartedDateValue.split("/")[1]);

            int monthsBetween = (currentYear - year) * 12 + (currentMonth - month);
            int yearsBetween = monthsBetween / 12;
            monthsBetween %= 12;

            messageExprience = monthsBetween > 0 ? monthsBetween + " tháng, " + yearsBetween + " năm kinh nghiệm" : yearsBetween + " năm kinh nghiệm";

            assert experienceMessageTextView != null;
            experienceMessageTextView.setText(messageExprience);

            monthPicker.setValue(month);
            yearPicker.setValue(year);
        }

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {
                tempExperience = EXPERIENCES.get(holder.getPos());

                assert haveExperience != null;
                if (tempExperience.equals(EXPERIENCES.get(0))) {
                    // Have experience
                    haveExperience.setVisibility(View.VISIBLE);
                } else {
                    // Not have experience
                    haveExperience.setVisibility(View.GONE);
                }
            }
        });

        // Save experience
        Button btnSaveExperience = dialogExperience.findViewById(R.id.btn_save_experience);
        assert btnSaveExperience != null;
        btnSaveExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experienceSelectedValue = tempExperience;
                experienceTextView.setText(experienceSelectedValue);
                isHaveExperience = experienceSelectedValue.equals(EXPERIENCES.get(0)); // Update is have experience
                assert monthPicker != null;
                assert yearPicker != null;
                experienceStartedDateValue = monthPicker.getValue() < 10 ? "0" + monthPicker.getValue() + "/" + yearPicker.getValue() : monthPicker.getValue() + "/" + yearPicker.getValue();
                tempExperience = "";
                dialogExperience.dismiss();
            }
        });

        // Close dialog
        ImageButton btnClose = dialogExperience.findViewById(R.id.btn_close_experience_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExperience.dismiss();
            }
        });
    }

    // Dialog birth
    @SuppressLint({"PrivateResource", "NotifyDataSetChanged"})
    private void showBirthDialog() {
        final BottomSheetDialog dialogBirth = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_birth_layout, null);
        dialogBirth.setContentView(view);
        dialogBirth.show();

        // Get date when user choose birth date
        TimePicker dayPicker = dialogBirth.findViewById(R.id.day_picker);
        int day = Integer.parseInt(birthDate.split("/")[0]);
        assert dayPicker != null;
        dayPicker.setValue(day);

        // Get month of date when user choose birth date
        TimePicker monthPicker = dialogBirth.findViewById(R.id.month_picker);
        int month = Integer.parseInt(birthDate.split("/")[1]);
        assert monthPicker != null;
        monthPicker.setValue(month);

        // Get year of date when user choose birth date
        TimePicker yearPicker = dialogBirth.findViewById(R.id.year_picker);
        int year = Integer.parseInt(birthDate.split("/")[2]);
        assert yearPicker != null;
        yearPicker.setValue(year);


        // Save chosen birth date
        Button buttonSaveDate = dialogBirth.findViewById(R.id.btn_save_birth);
        assert buttonSaveDate != null;
        buttonSaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfBirth = dayPicker.getValue();
                monthOfBirth = monthPicker.getValue();
                yearOfBirth = yearPicker.getValue();

                birthDate = dayOfBirth < 10 ? "0" + dayOfBirth + "/" : dayOfBirth + "/";
                birthDate += monthOfBirth < 10 ? "0" + monthOfBirth + "/" : monthOfBirth + "/";
                birthDate += yearOfBirth;

                birthDayTextView.setText(birthDate);
                dialogBirth.dismiss();
            }
        });


        // Close dialog
        ImageButton btnClose = dialogBirth.findViewById(R.id.btn_close_birth_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBirth.dismiss();
            }
        });
    }
}