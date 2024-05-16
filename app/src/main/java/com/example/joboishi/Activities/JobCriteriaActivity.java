package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.CityChosenAdpater;
import com.example.joboishi.Adapters.SelectedJobAdapter;
import com.example.joboishi.Adapters.WorkFormAdapter;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.JobCriteriaApiResponse;
import com.example.joboishi.Api.JobCriteriaRequest;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.SelectSalaryFragment;
import com.example.joboishi.Models.WorkForm;
import com.example.joboishi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobCriteriaActivity extends AppCompatActivity {

    private final SelectSalaryFragment salaryFragment = new SelectSalaryFragment();
    private final int REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE = 8080;
    private final int REQUEST_TO_ADDRESS_ACTIVITY_CODE = 8081;
    private final ArrayList<String> selectedJobs = new ArrayList<>();
    private final ArrayList<String> selectedCities = new ArrayList<>();
    private final ArrayList<WorkForm> workForms = new ArrayList<>();
    private final ArrayList<String> workFormChosen = new ArrayList<>();
    private final int USER_ID = 1;
    private AppCompatCheckBox checkIsRemote;
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

        // Init data
        workForms.add(new WorkForm("Toàn thời gian", false));
        workForms.add(new WorkForm("Bán thời gian", false));
        workForms.add(new WorkForm("Mỗi ngày", false));
        workForms.add(new WorkForm("Thực tập", false));

        // Get view
        checkIsRemote = findViewById(R.id.check_is_remote);
        RecyclerView listSelectedJob = findViewById(R.id.list_job_select);
        RecyclerView listSelectedCity = findViewById(R.id.list_city_selected);
        RecyclerView listWorkForms = findViewById(R.id.list_work_form);
        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.job_criteria_toolbar_title);

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCriteriaActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
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
        LinearLayoutManager layoutManagerCity = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerCity.setOrientation(LinearLayoutManager.HORIZONTAL);
        listSelectedCity.setLayoutManager(layoutManagerCity);

        // List work forms
        WorkFormAdapter workFormAdapter = new WorkFormAdapter(this, workForms);
        workFormAdapter.setItemClickListener(new WorkFormAdapter.ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(WorkFormAdapter.MyViewHolder holder, int position) {
                if (!workFormChosen.contains(workForms.get(position).getWorkFormName())) {
                    workFormChosen.add(workForms.get(position).getWorkFormName());
                }
                workForms.get(position).setChosen(!workForms.get(position).isChosen());
                workFormAdapter.notifyDataSetChanged();
            }
        });
        listWorkForms.setAdapter(workFormAdapter);
        // Management layout of recycler view
        LinearLayoutManager layoutManagerWorkForm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerWorkForm.setOrientation(LinearLayoutManager.VERTICAL);
        listWorkForms.setLayoutManager(layoutManagerWorkForm);


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

        // Button Save JobCriteria
        Button buttonSaveJobCriteria = findViewById(R.id.btn_save_salary);
        buttonSaveJobCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserJobCriteria();
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

    public void updateUserJobCriteria() {
        // Get user data from api
        StringBuilder jobPositions = new StringBuilder();
        StringBuilder cities = new StringBuilder();
        StringBuilder userWorkForms = new StringBuilder();

        String salary = salaryFragment.getMinSalarySelected() + "," + salaryFragment.getMaxSalarySelected();


        for (int i = 0; i < selectedJobs.size(); i++) {
            if (i == selectedJobs.size() - 1) {
                jobPositions.append(selectedJobs.get(i));
            } else {
                jobPositions.append(selectedJobs.get(i)).append(",");
            }
        }

        for (int i = 0; i < selectedCities.size(); i++) {
            if (i == selectedCities.size() - 1) {
                cities.append(selectedCities.get(i));
            } else {
                cities.append(selectedCities.get(i)).append(",");
            }
        }


        for (int i = 0; i < workFormChosen.size(); i++) {
            if (i == workFormChosen.size() - 1) {
                userWorkForms.append(workFormChosen.get(i));
            } else {
                userWorkForms.append(workFormChosen.get(i)).append(",");
            }
        }

        Log.d("work_forms", userWorkForms.toString());


        JobCriteriaRequest request = new JobCriteriaRequest();
        request.setUser_id(USER_ID);
        request.setJob_location(String.valueOf(cities));
        request.setJob_salary(salary);
        request.setIs_remote(checkIsRemote.isChecked() ? 1 : 0);
        request.setJob_position(String.valueOf(jobPositions));
        request.setWorking_form(String.valueOf(userWorkForms));


        UserApi userApi = ApiClient.getUserAPI();
        Call<JobCriteriaApiResponse> callUser = userApi.updateJobCriteria(request);
        callUser.enqueue(new Callback<JobCriteriaApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<JobCriteriaApiResponse> call, @NonNull Response<JobCriteriaApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(JobCriteriaActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JobCriteriaActivity.this, "Đã xảy ra lỗi trong quá trình xử lý, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JobCriteriaApiResponse> call, @NonNull Throwable t) {
                Log.d("error", t.toString());
            }
        });
    }
}