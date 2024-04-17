    package com.example.joboishi.activities;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.joboishi.R;
    import com.example.joboishi.adapters.CityAdapter;
    import com.example.joboishi.adapters.CityChosenAdpater;
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
                            // Thêm major vào danh sách majorsChosen
                            listCityChosen.add(clickedCity);
                        } else {
                            listCityChosen.remove(clickedCity);
                        }
                        // Thay đổi trạng thái của CheckBox khi click vào CardView
                        clickedCity.setChecked_city(!clickedCity.getChecked_city());

                        cityAdapter.notifyItemChanged(position);
                        cityChosenAdpater.notifyDataSetChanged();

                        // Hiển thị Toast để kiểm tra
                        Log.d("TAG", listCityChosen.size() + "");
                    }
                }
            });

            // Set adapter for citychosen
            cityChosenAdpater = new CityChosenAdpater(this, listCityChosen);

            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
            layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
            addressLayoutBinding.showListCitiesChosen.setLayoutManager(layoutManager2);
            addressLayoutBinding.showListCitiesChosen.setAdapter(cityChosenAdpater);

        }
    }