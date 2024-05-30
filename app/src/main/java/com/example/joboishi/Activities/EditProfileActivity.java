package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.example.joboishi.Api.UserResponse;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.SelectBirthFragment;
import com.example.joboishi.Fragments.SelectCityFragment;
import com.example.joboishi.Fragments.SelectCountryFragment;
import com.example.joboishi.Fragments.SelectEducationFragment;
import com.example.joboishi.Fragments.SelectExperienceFragment;
import com.example.joboishi.Fragments.SelectGenderFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ActivityEditProfileBinding;
import com.makeramen.roundedimageview.RoundedImageView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ArrayList<CountryApiResponse> tempCountry = new ArrayList<>();
    private final ArrayList<ProvinceApiResponse> tempCity = new ArrayList<>();
    private final SelectCountryFragment countryFragment = new SelectCountryFragment();
    private final SelectCityFragment cityFragment = new SelectCityFragment();
    private final SelectBirthFragment birthFragment = new SelectBirthFragment();
    private final SelectGenderFragment genderFragment = new SelectGenderFragment();
    private final SelectEducationFragment eduFragment = new SelectEducationFragment();
    private final SelectExperienceFragment experienceFragment = new SelectExperienceFragment();
    private final int STATUS_NO_INTERNET = 0;
    private final int STATUS_GOOD_INTERNET = 2;
    private final String DEFAULT_COUNTRY_STR = "Chọn quốc gia";
    private int USER_ID = 0;
    private UserResponse userData = new UserResponse();
    private TextView countryTextView; // use to display country of user
    private TextView cityTextView; // use to display city of user
    private TextView genderTextView; // use to display gender of user
    private TextView eduTextView; // use to display edu of user
    private TextView experienceTextView; // use to display experience of user
    private TextView birthDayTextView; // use to display birth date of user
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirst = true;
    private ActivityEditProfileBinding binding;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private RoundedImageView userAvatar;
    // private LoadingDialog loadingDialog;
    private TextView errorFirstnameTextView;
    private TextView errorLastnameTextView;
    private boolean isDataChanged = false;

    // Func to check if a string contains char
    // Ex: "Vietnam" will contains "vi" , "am" , "nam" , "iet"
    // Pattern like *."char".*
    public static boolean checkPattern(String inputString, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inputString);
        return m.find();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Register broadcast receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerInternetBroadcastReceiver();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Default value
        CountryApiResponse vnCountry = new CountryApiResponse("Vietnam", "84", "success");
        countryData.add(vnCountry);
        tempCountry.addAll(countryData);

        // Get View
        countryTextView = findViewById(R.id.selected_country_label);
        firstNameTextView = findViewById(R.id.firstname_textview);
        lastNameTextView = findViewById(R.id.lastname_textview);
        countryTextView = findViewById(R.id.selected_country_label);
        userAvatar = findViewById(R.id.user_avatar);
        errorFirstnameTextView = findViewById(R.id.error_firstname_textview);
        errorLastnameTextView = findViewById(R.id.error_lastname_textview);

        // Lấy giá trị từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        USER_ID = sharedPref.getInt("user_id", 0);

        if (USER_ID == 0) {
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Init default data
        String countrySelectedValue = DEFAULT_COUNTRY_STR;
        countryTextView.setText(countrySelectedValue);
        countryFragment.setCountryTextView(countryTextView);
        countryFragment.setCountrySelectedValue(countrySelectedValue);
        countryFragment.setCountryData(countryData);
        countryFragment.setTemp(tempCountry);

        cityTextView = findViewById(R.id.selected_city_label);
        // Hard City of user
        String citySelectedValue = "Thành phố Hồ Chí Minh";
        cityTextView.setText(citySelectedValue);
        cityFragment.setCityData(provinceData);
        cityFragment.setCityTextView(cityTextView);
        cityFragment.setCitySelectedValue(citySelectedValue);

        genderTextView = findViewById(R.id.selected_gender);
        // Hard gender of user
        String genderSelectedValue = genderData.get(0);
        genderTextView.setText(genderSelectedValue);
        genderFragment.setGenderTextView(genderTextView);
        genderFragment.setGenderData(genderData);
        genderFragment.setGenderSelectedValue(genderSelectedValue);

        eduTextView = findViewById(R.id.selected_edu);
        // Hard edu of user
        String eduSelectedValue = EDUCATIONS.get(0);
        eduTextView.setText(eduSelectedValue);
        eduFragment.setEduTextView(eduTextView);
        eduFragment.setEduData(EDUCATIONS);
        eduFragment.setEduSelectedValue(eduSelectedValue);

        experienceTextView = findViewById(R.id.selected_experience);
        // Hard experience of user
        String experienceSelectedValue = EXPERIENCES.get(1);
        experienceTextView.setText(experienceSelectedValue);
        experienceFragment.setExperienceSelectedValue(experienceSelectedValue);
        // experience of user
        experienceFragment.setHaveExperience(false);
        experienceFragment.setExperienceData(EXPERIENCES);
        experienceFragment.setExperienceTextView(experienceTextView);
        // Begin employment time
        String experienceStartedDateValue = "04/2022";
        experienceFragment.setExperienceStartedDateValue(experienceStartedDateValue);

        birthDayTextView = findViewById(R.id.selected_birth);
        // birth date of user
        String birthDate = "24/04/2004";
        birthDayTextView.setText(birthDate);
        birthFragment.setBirthDate(birthDate);
        birthFragment.setBirthDayTextView(birthDayTextView);

        // Get data from intent
        Intent intent = getIntent();
        userData = (UserResponse) intent.getSerializableExtra("user_data");

        if (userData != null) {
            isDataChanged = false;
            Glide.with(getBaseContext())
                    .load(userData.getPhotoUrl())
                    .error(R.drawable.avatar_thinking_svgrepo_com)
                    .into(userAvatar);

            firstNameTextView.setText(userData.getFirstName());
            lastNameTextView.setText(userData.getLastName());

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

            if (userData.getTimeStartingWorking() == null) {
                experienceFragment.setHaveExperience(false);
            } else {
                experienceTextView.setText(userData.getTimeStartingWorking());
                experienceFragment.setExperienceSelectedValue(EXPERIENCES.get(0)); // Have experience
                experienceFragment.setHaveExperience(true);
                experienceFragment.setExperienceStartedDateValue(userData.getTimeStartingWorking());
            }
        }


        // Show dialog loading when fetching data
        // loadingDialog = new LoadingDialog(this);
        // loadingDialog.show();

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.edit_profile_toolbar_title);

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("data_user_updated", isDataChanged);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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


        // Lister swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        registerInternetBroadcastReceiver();
                    }
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
                    countryData.clear();
                    tempCountry.clear();
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


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
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

        String DEFAULT_CITY_STR = "Chọn thành phố";
        if (city.equals(DEFAULT_CITY_STR)) {
            city = null;
        }

        String DEFAULT_BIRTH_STR = "Chọn ngày tháng năm sinh của bạn";
        if (birth.equals(DEFAULT_BIRTH_STR)) {
            birth = null;
        }

        String DEFAULT_GENDER_STR = "Chọn giới tính của bạn";
        if (gender.equals(DEFAULT_GENDER_STR)) {
            gender = null;
        }

        String DEFAULT_EDU_STR = "Chọn trình độ học vấn của bạn";
        if (edu.equals(DEFAULT_EDU_STR)) {
            edu = null;
        }

        String DEFAULT_NO_EXPERIENCE_STR = "Tôi chưa có kinh nghiệm";
        String DEFAULT_HAVE_EXPERIENCE_STR = "Tôi đã có kinh nghiệm";
        if (timeStartingWork.equals(DEFAULT_NO_EXPERIENCE_STR) || timeStartingWork.equals(DEFAULT_HAVE_EXPERIENCE_STR)) {
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
                    // Toast.makeText(EditProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EditProfileActivity.this, "Thành công",
                            "Cập nhật thông tin thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EditProfileActivity.this, R.font.helvetica_regular));

                    isDataChanged = true;
                } else {
                    Log.d("UPDATE_USER_ERROR", "ERROR");
                    // Toast.makeText(EditProfileActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EditProfileActivity.this, "Thất bại",
                            "Đã xảy ra lỗi trong quá trình cập nhật thông tin",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EditProfileActivity.this, R.font.helvetica_regular));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("UPDATE_USER_ERROR", t.toString());
                // Toast.makeText(EditProfileActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                MotionToast.Companion.createToast(EditProfileActivity.this, "Thất bại",
                        "Đã xảy ra lỗi trong quá trình cập nhật thông tin",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(EditProfileActivity.this, R.font.helvetica_regular));
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
                    // Toast.makeText(EditProfileActivity.this, "Ảnh đại diện của bạn đã được cập nhật", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EditProfileActivity.this, "Thành công",
                            "Cập nhật ảnh đại diện thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EditProfileActivity.this, R.font.helvetica_regular));
                    isDataChanged = true;

                }

                @Override
                public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                    // Toast.makeText(EditProfileActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật ảnh", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EditProfileActivity.this, "Thất bại",
                            "Đã xảy ra lỗi trong quá trình cập nhật ảnh đại diện",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EditProfileActivity.this, R.font.helvetica_regular));

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