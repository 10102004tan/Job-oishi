package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.CityChosenAdpater;
import com.example.joboishi.Adapters.SelectedJobAdapter;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.SelectSalaryFragment;
import com.example.joboishi.R;

import java.util.ArrayList;

public class JobCriteriaActivity extends AppCompatActivity {

    private final SelectSalaryFragment salaryFragment = new SelectSalaryFragment();
    private final int REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE = 8080;
    private final int REQUEST_TO_ADDRESS_ACTIVITY_CODE = 8081;
    private final ArrayList<String> selectedJobs = new ArrayList<>();
    private final ArrayList<String> selectedCities = new ArrayList<>();
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private SelectedJobAdapter majorChosenAdapter;
    private CityChosenAdpater cityChosenAdpater;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_criteria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get view
        RecyclerView listSelectedJob = findViewById(R.id.list_job_select);
        RecyclerView listSelectedCity = findViewById(R.id.list_city_selected);

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.job_criteria_toolbar_title);

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Note: Handle later
            }
        });

        // List selected job
        majorChosenAdapter = new SelectedJobAdapter(this, selectedJobs);
        listSelectedJob.setAdapter(majorChosenAdapter);
        // Management layout of recycler view
        LinearLayoutManager layoutManagerJob = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerJob.setOrientation(LinearLayoutManager.HORIZONTAL);
        listSelectedJob.setLayoutManager(layoutManagerJob);
        // Handle remove item selected when touch in
        majorChosenAdapter.setItemClickListener(new SelectedJobAdapter.ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(SelectedJobAdapter.MyViewHolder holder, int position) {
                String job = selectedJobs.get(position);
                selectedJobs.remove(job);
                majorChosenAdapter.notifyDataSetChanged();
            }
        });


        // List selected city
        cityChosenAdpater = new CityChosenAdpater(this, selectedCities);
        listSelectedCity.setAdapter(cityChosenAdpater);
        // Management layout of recycler view
        // Management layout of recycler view
        LinearLayoutManager layoutManagerCity = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerCity.setOrientation(LinearLayoutManager.HORIZONTAL);
        listSelectedCity.setLayoutManager(layoutManagerCity);


        // Handle remove item selected when touch in
        cityChosenAdpater.setItemClickListener(new CityChosenAdpater.ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(CityChosenAdpater.MyViewHolder holder, int position) {
                String city = selectedCities.get(position);
                selectedCities.remove(city);
                majorChosenAdapter.notifyDataSetChanged();
            }
        });

        // Salary Textview
        TextView salaryTextView = findViewById(R.id.salary_text_view);
        salaryTextView.setText("1 - 9 Tr");
        salaryFragment.setSalaryTextView(salaryTextView);
        salaryFragment.setMinSalarySelected(1);
        salaryFragment.setMaxSalarySelected(9);


        // Button choose salary
        LinearLayout buttonChooseSalary = findViewById(R.id.btn_choose_salary);
        buttonChooseSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
                myBottomSheetDialogFragment.setFragment(salaryFragment);
                myBottomSheetDialogFragment.show(getSupportFragmentManager(), "MyBottomSheetDialogFragmentTag");
            }
        });

        // Go to add job position screen
        LinearLayout buttonGotoAddJobPosition = findViewById(R.id.btn_choose_job_pos);
        buttonGotoAddJobPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCriteriaActivity.this, RegisterMajorActivity.class);
                intent.putExtra("majors", selectedJobs);
                startActivityForResult(intent, REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE);
            }
        });

        // Go to add city screen
        LinearLayout buttonGotoAddCity = findViewById(R.id.btn_choose_city_name);
        buttonGotoAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCriteriaActivity.this, AddressActivity.class);
                intent.putExtra("cities", selectedCities);
                startActivityForResult(intent, REQUEST_TO_ADDRESS_ACTIVITY_CODE);
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> value = (ArrayList<String>) data.getSerializableExtra("majorsChosen");
                if (value != null) {
                    selectedJobs.clear();
                    selectedJobs.addAll(value);
                    majorChosenAdapter.notifyDataSetChanged();
                }
            }
        }

        if (requestCode == REQUEST_TO_ADDRESS_ACTIVITY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> value = (ArrayList<String>) data.getSerializableExtra("cities");
                if (value != null) {
                    selectedCities.clear();
                    selectedCities.addAll(value);
                    cityChosenAdpater.notifyDataSetChanged();
                }
            }
        }
    }
}