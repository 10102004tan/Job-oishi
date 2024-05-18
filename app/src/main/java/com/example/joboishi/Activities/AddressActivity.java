package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.CityAdapter;
import com.example.joboishi.Adapters.CityChosenAdpater;
import com.example.joboishi.Models.CityMajors;
import com.example.joboishi.Models.RegisterMajors;
import com.example.joboishi.R;
import com.example.joboishi.databinding.AddressLayoutBinding;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    ArrayList<RegisterMajors> majorsChosen;
    ArrayList<CityMajors> listCity = new ArrayList<>();
    ArrayList<String> listCityChosen = new ArrayList<>();
    CityAdapter cityAdapter;
    CityChosenAdpater cityChosenAdpater;
    private AddressLayoutBinding addressLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this));
        setContentView(addressLayoutBinding.getRoot());


        // Get View
        Button btnDone = findViewById(R.id.btnDone);

        // Get data from intent, from Job criteria Activity
        Intent intent = getIntent();
        // Use for check caller activity
        String caller = intent.getStringExtra("caller");
        assert caller != null;
        if (caller.equals("JobCriteriaActivity")) {
            btnDone.setText(R.string.btn_save_label);
        }
        // Get data from intent, from Job criteria Activity
        ArrayList<String> city = (ArrayList<String>) intent.getSerializableExtra("cities");
        assert city != null;
        if (!city.isEmpty()) {
            listCityChosen.clear();
            listCityChosen.addAll(city);
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