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
    import com.example.joboishi.databinding.AddressLayoutBinding;
    import com.example.joboishi.models.RegisterMajors;

    import java.util.ArrayList;

    public class AddressActivity extends AppCompatActivity {

        private AddressLayoutBinding addressLayoutBinding;
        ArrayList<RegisterMajors> majorsChosen;
        ArrayList<String> listCity = new ArrayList<>();
        CityAdapter cityAdapter;

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

            listCity.add("Thành phố Hà Nội");
            listCity.add("Thành phố Đà Nẵng");
            listCity.add("Thành phố Hải Phòng");
            listCity.add("Thành phố Hồ Chí Minh");
            listCity.add("Thành phố Quy Nhơn");
            listCity.add("Thành phố Vinh");
            listCity.add("Thành phố Cần Thơ");

            cityAdapter = new CityAdapter(this, listCity);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            addressLayoutBinding.showListCities.setLayoutManager(layoutManager);
            addressLayoutBinding.showListCities.setAdapter(cityAdapter);


        }
    }