package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.CityAdapter;
import com.example.joboishi.Adapters.CityChosenAdpater;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.JobCriteriaApiResponse;
import com.example.joboishi.Api.JobCriteriaRequest;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Models.CityMajors;
import com.example.joboishi.Models.RegisterMajors;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.LoadingDialog;
import com.example.joboishi.databinding.AddressLayoutBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class AddressActivity extends AppCompatActivity {

    ArrayList<RegisterMajors> majorsChosen;
    ArrayList<CityMajors> listCity = new ArrayList<>();
    ArrayList<String> listCityChosen = new ArrayList<>();
    CityAdapter cityAdapter;
    CityChosenAdpater cityChosenAdpater;
    private AddressLayoutBinding addressLayoutBinding;
    private ArrayList<String> majors = new ArrayList<>();
    private int USER_ID = 0;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(addressLayoutBinding.getRoot());


        // Get View
        Button btnDone = findViewById(R.id.btnDone);
        loadingDialog = new LoadingDialog(this);
        TextView toolbarTitle = findViewById(R.id.toolbar_text_title);
        ImageButton buttonBackToolbar = findViewById(R.id.btn_toolbar_back);

        // Lấy giá trị từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        USER_ID = sharedPref.getInt("user_id", 0);

        if (USER_ID == 0) {
            Intent intent = new Intent(AddressActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Get data from intent, from Job criteria Activity
        Intent intent = getIntent();
        // Use for check caller activity
        String caller = intent.getStringExtra("caller");
        boolean isFirstLogin = intent.getBooleanExtra("is_first_login", false);
        assert caller != null;
        if (caller.equals("JobCriteriaActivity")) {
            btnDone.setText(R.string.btn_save_label);
            // Get data from intent, from Job criteria Activity
            ArrayList<String> city = (ArrayList<String>) intent.getSerializableExtra("cities");
            assert city != null;
            if (!city.isEmpty()) {
                listCityChosen.clear();
                listCityChosen.addAll(city);
            }
        } else if (caller.equals("RegisterMajorActivity")) {
            majors = (ArrayList<String>) intent.getSerializableExtra("majorsChosen");
            if (isFirstLogin) {
                buttonBackToolbar.setVisibility(View.GONE);
                toolbarTitle.setVisibility(View.GONE);
            }
        }


        listCity.add(new CityMajors("Thành phố Hồ Chí Minh", false));
        listCity.add(new CityMajors("Thành phố Đà Nẵng", false));
        listCity.add(new CityMajors("Thành phố Hải Phòng", false));
        listCity.add(new CityMajors("Thành phố Hà Nội", false));
        listCity.add(new CityMajors("Thành phố Qui Nhơn", false));
        listCity.add(new CityMajors("Thành phố Vinh", false));
        listCity.add(new CityMajors("Thành phố Cần Thơ", false));

            /*
            for (CityMajors city : listCity) {
                if (city.getChecked_city()) {
                    listCityChosen.add(city.getName_city());
                }
            }
             */

        updateChosenCountTextView();
        updateButtonBackground();

        // Set adapter for city
        cityAdapter = new CityAdapter(this, listCity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        addressLayoutBinding.showListCities.setLayoutManager(layoutManager);
        addressLayoutBinding.showListCities.setAdapter(cityAdapter);

        cityAdapter.setItemClickListener(new CityAdapter.ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(CityAdapter.MyViewHolder holder, int position) {
                if (position != RecyclerView.NO_POSITION) {
//                        updateChosenCountTextView();
                    CityMajors clickedCity = listCity.get(position);
                    // Kiểm tra nếu major chưa được chọn
                    if (!clickedCity.getChecked_city()) {
                        if (listCityChosen.size() < 3) {
                            // Thêm major vào danh sách majorsChosen
                            listCityChosen.add(clickedCity.getName_city());
                            updateChosenCountTextView();
                        } else {
                            Toast.makeText(AddressActivity.this, "Bạn đã chọn tối đa 3 địa điểm.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } else {
                        listCityChosen.remove(clickedCity.getName_city());
                        updateChosenCountTextView();
                    }
                    // Thay đổi trạng thái của CheckBox khi click vào CardView
                    clickedCity.setChecked_city(!clickedCity.getChecked_city());

                    cityAdapter.notifyItemChanged(position);
                    cityChosenAdpater.notifyDataSetChanged();
                    updateButtonBackground();
                }
            }
        });

        // Set adapter for city chosen
        cityChosenAdpater = new CityChosenAdpater(this, listCityChosen);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        addressLayoutBinding.showListCitiesChosen.setLayoutManager(layoutManager2);
        addressLayoutBinding.showListCitiesChosen.setAdapter(cityChosenAdpater);
        handleMajorAutoCheck();
        cityChosenAdpater.setItemClickListener(new CityChosenAdpater.ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(CityChosenAdpater.MyViewHolder holder, int position) {
                String major = listCityChosen.get(position);
                listCityChosen.remove(major);

                // Xóa major khỏi danh sách majorsChosen
                cityChosenAdpater.notifyDataSetChanged();
                updateChosenCountTextView();
                // Cập nhật trạng thái của major trong danh sách majors ban đầu
                for (int i = 0; i < listCity.size(); i++) {
                    CityMajors originalCity = listCity.get(i);
                    if (originalCity.getName_city().equals(major)) {
                        originalCity.setChecked_city(false);
                        cityAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                updateButtonBackground(); // Gọi hàm để cập nhật nền của nút
            }
        });

        // Button Gợi Ý
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caller.equals("JobCriteriaActivity")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("cities", listCityChosen);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (caller.equals("RegisterMajorActivity")) {
                    // loadingDialog.show();
                    StringBuilder jobPositions = new StringBuilder();
                    StringBuilder cities = new StringBuilder();


                    for (int i = 0; i < majors.size(); i++) {
                        if (i == majors.size() - 1) {
                            jobPositions.append(majors.get(i));
                        } else {
                            jobPositions.append(majors.get(i)).append(",");
                        }
                    }

                    for (int i = 0; i < listCityChosen.size(); i++) {
                        if (i == listCityChosen.size() - 1) {
                            cities.append(listCityChosen.get(i));
                        } else {
                            cities.append(listCityChosen.get(i)).append(",");
                        }
                    }


                    JobCriteriaRequest request = new JobCriteriaRequest();
                    request.setUser_id(USER_ID);
                    request.setJob_location(String.valueOf(cities));
                    request.setJob_position(String.valueOf(jobPositions));


                    UserApi userApi = ApiClient.getUserAPI();
                    Call<JobCriteriaApiResponse> callUser = userApi.updateJobCriteria(request);
                    callUser.enqueue(new Callback<JobCriteriaApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<JobCriteriaApiResponse> call, @NonNull Response<JobCriteriaApiResponse> response) {
                            if (response.isSuccessful()) {
                                loadingDialog.cancel();
                                Intent intentHome = new Intent(AddressActivity.this, HomeActivity.class);
                                startActivity(intentHome);
                                finish();
                            } else {
                                MotionToast.Companion.createToast(AddressActivity.this, "Thất bại",
                                        "Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(AddressActivity.this, R.font.helvetica_regular));
                                // Toast.makeText(JobCriteriaActivity.this, "Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JobCriteriaApiResponse> call, @NonNull Throwable t) {
                            Log.d("error", t.toString());
                        }
                    });
                }
            }
        });
    }

    private boolean hasCitiesChosen() {
        return listCityChosen != null && !listCityChosen.isEmpty();
    }

    private void updateButtonBackground() {
        AppCompatButton btnDone = addressLayoutBinding.btnDone;
        if (hasCitiesChosen()) {
            btnDone.setBackgroundResource(R.drawable.background_button);
            btnDone.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnDone.setBackgroundResource(R.drawable.background_button_disable);
            btnDone.setTextColor(getResources().getColor(R.color.btn_disable));
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateChosenCountTextView() {
        TextView countChosenAllow = addressLayoutBinding.countChosenAllow;
        if (listCityChosen != null) {
            int count = listCityChosen.size();
            int remainingCount = 3 - count;
            if (remainingCount > 0) {
                countChosenAllow.setText("Bạn còn có thể chọn thêm " + remainingCount + " lựa chọn.");
            } else {
                countChosenAllow.setText("Bạn đã chọn tối đa.");
            }
        } else {
            countChosenAllow.setText("Bạn có thể lựa chọn tối đa 3 địa điểm.");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleMajorAutoCheck() {
        for (int position = 0; position < listCity.size(); position++) {
            for (int check = 0; check < listCityChosen.size(); check++) {
                if (listCity.get(position).getName_city().equals(listCityChosen.get(check))) {
                    listCity.get(position).setChecked_city(true);
                }
            }
        }
        cityChosenAdpater.notifyDataSetChanged();
    }
}