    package com.example.joboishi.activities;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatButton;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.joboishi.R;
    import com.example.joboishi.adapters.CityAdapter;
    import com.example.joboishi.adapters.CityChosenAdpater;
    import com.example.joboishi.adapters.RegisterMajorAdapter;
    import com.example.joboishi.databinding.AddressLayoutBinding;
    import com.example.joboishi.models.CityMajors;
    import com.example.joboishi.models.RegisterMajors;

    import java.util.ArrayList;

    public class AddressActivity extends AppCompatActivity {

        private AddressLayoutBinding addressLayoutBinding;
        ArrayList<RegisterMajors> majorsChosen;
        ArrayList<CityMajors> listCity = new ArrayList<>();
        ArrayList<CityMajors> listCityChosen = new ArrayList<>();
        CityAdapter cityAdapter;
        CityChosenAdpater cityChosenAdpater;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this));
            setContentView(addressLayoutBinding.getRoot());

            // Trong onCreate hoặc một phương thức tương tự
            Intent intent = getIntent();
            if (intent != null) {

                majorsChosen = (ArrayList<RegisterMajors>) intent.getSerializableExtra("majorsChosen");
                if (majorsChosen != null) {
                    Log.d("test", majorsChosen.size() +"");
                }
            }

            listCity.add(new CityMajors("Thành phố Hồ Chí Minh", true));
            listCity.add(new CityMajors("Thành phố Đà Nẵng", false));
            listCity.add(new CityMajors("Thành phố Hải Phòng", true));
            listCity.add(new CityMajors("Thành phố Hà Nội", false));
            listCity.add(new CityMajors("Thành phố Qui Nhơn", false));
            listCity.add(new CityMajors("Thành phố Vinh", false));
            listCity.add(new CityMajors("Thành phố Cần Thơ", false));

            for (CityMajors city : listCity) {
                if (city.getChecked_city()) {
                    listCityChosen.add(city);
                }
            }

            updateChosenCountTextView();
            updateButtonBackground();
            // Set adapter for city
            cityAdapter = new CityAdapter(this, listCity);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            addressLayoutBinding.showListCities.setLayoutManager(layoutManager);
            addressLayoutBinding.showListCities.setAdapter(cityAdapter);

            cityAdapter.setItemClickListener(new CityAdapter.ItemClickListener() {
                @Override
                public void onItemClick(CityAdapter.MyViewHolder holder, int position) {
                    if (position != RecyclerView.NO_POSITION) {
//                        updateChosenCountTextView();
                        CityMajors clickedCity = listCity.get(position);
                        // Kiểm tra nếu major chưa được chọn
                        if (!clickedCity.getChecked_city()) {
                            if (listCityChosen.size() < 3) {
                                // Thêm major vào danh sách majorsChosen
                                listCityChosen.add(clickedCity);
                                updateChosenCountTextView();
                            } else {
                                Toast.makeText(AddressActivity.this, "Bạn đã chọn tối đa 3 địa điểm.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            listCityChosen.remove(clickedCity);
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

            // Set adapter for citychosen
            cityChosenAdpater = new CityChosenAdpater(this, listCityChosen);

            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
            layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
            addressLayoutBinding.showListCitiesChosen.setLayoutManager(layoutManager2);
            addressLayoutBinding.showListCitiesChosen.setAdapter(cityChosenAdpater);

            cityChosenAdpater.setItemClickListener(new CityChosenAdpater.ItemClickListener() {
                @Override
                public void onItemClick(CityChosenAdpater.MyViewHolder holder, int position) {
                    CityMajors major = listCityChosen.get(position);
                    listCityChosen.remove(major);

                    // Xóa major khỏi danh sách majorsChosen
                    cityChosenAdpater.notifyDataSetChanged();
                    updateChosenCountTextView();
                    // Cập nhật trạng thái của major trong danh sách majors ban đầu
                    for (int i = 0; i < listCity.size(); i++) {
                        CityMajors originalCity = listCity.get(i);
                        if (originalCity.equals(major)) {
                            originalCity.setChecked_city(false);
                            cityAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                    updateButtonBackground(); // Gọi hàm để cập nhật nền của nút

                }
            });
            // Button Gợi Ý
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

    }