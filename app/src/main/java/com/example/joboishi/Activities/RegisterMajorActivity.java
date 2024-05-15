package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.joboishi.Adapters.MajorChosenAdapter;
import com.example.joboishi.Adapters.RegisterMajorAdapter;
import com.example.joboishi.Api.JobSearchAPI;
import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.R;
import com.example.joboishi.databinding.RegisterMajorLayoutBinding;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMajorActivity extends AppCompatActivity{
    private RegisterMajorLayoutBinding registerMajorLayoutBinding;
    private ArrayList<JobSearch> majors = new ArrayList<>();
    private final ArrayList<JobSearch> majorsChosen = new ArrayList<>();
    private RegisterMajorAdapter registerMajorAdapter;
    private MajorChosenAdapter majorChosenAdapter;
    private JobSearchAPI jobSearchAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMajorLayoutBinding = RegisterMajorLayoutBinding.inflate(getLayoutInflater());
        setContentView(registerMajorLayoutBinding.getRoot());

        // Get data from intent, from Job criteria Activity
        Intent intent = getIntent();
        ArrayList<JobSearch> majors = (ArrayList<JobSearch>) intent.getSerializableExtra("majors");
        assert majors != null;
        if (!majors.isEmpty()) {
            majorsChosen.clear();
            majorsChosen.addAll(majors);
        }

        EditText input = registerMajorLayoutBinding.inputMajor;

        // Add TextWatcher to EditText
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        // Khởi tạo Retrofit và JobSearchAPI
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JobSearchAPI.BASE_URL) // Thay bằng base URL thực tế
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSearchAPI = retrofit.create(JobSearchAPI.class);

        RecyclerView recyclerView = registerMajorLayoutBinding.showListMajor;

        // Khởi tạo adapter và thiết lập itemClickListener
        registerMajorAdapter = new RegisterMajorAdapter(this, majors);
        registerMajorAdapter.setItemClickListener(new RegisterMajorAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RegisterMajorAdapter.MyViewHolder holder, int position) {
                handleMajorClick(position);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        registerMajorLayoutBinding.showListMajor.setLayoutManager(layoutManager);
        registerMajorLayoutBinding.showListMajor.setAdapter(registerMajorAdapter);


        getJobs(); // Gọi API để lấy danh sách công việc

        majorChosenAdapter = new MajorChosenAdapter(this, majorsChosen);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        registerMajorLayoutBinding.showListMajorsChosen.setLayoutManager(layoutManager2);
        registerMajorLayoutBinding.showListMajorsChosen.setAdapter(majorChosenAdapter);
        majorChosenAdapter.setItemClickListener(new MajorChosenAdapter.ItemClickListener() {
            @Override
            public void onItemClick(MajorChosenAdapter.MyViewHolder holder, int position) {
                handleChosenMajorClick(position);
            }
        });

        AppCompatButton btnNext = registerMajorLayoutBinding.btnNextAdress;
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasMajorsChosen()) {
                    // Handle mul screen here

                    /*
                    Intent intent = new Intent(RegisterMajorActivity.this, AddressActivity.class);
                    intent.putExtra("majorsChosen", majorsChosen);
                    startActivity(intent);
                     */


                    //
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("majorsChosen", majorsChosen);

                    // Đặt kết quả trả về và kết thúc Activity B
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        updateChosenCountTextView();
        updateButtonBackground();
    }


    private boolean hasMajorsChosen() {
        return majorsChosen != null && !majorsChosen.isEmpty();
    }

    private void updateButtonBackground() {
        AppCompatButton btnNext = registerMajorLayoutBinding.btnNextAdress;
        if (hasMajorsChosen()) {
            btnNext.setBackgroundResource(R.drawable.background_button);
            btnNext.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnNext.setBackgroundResource(R.drawable.background_button_disable);
            btnNext.setTextColor(getResources().getColor(R.color.btn_disable));
        }
    }

    private void updateChosenCountTextView() {
        TextView countChosenAllow = registerMajorLayoutBinding.countChosenAllow;
        if (majorsChosen != null) {
            int count = majorsChosen.size();
            int remainingCount = 5 - count;
            if (remainingCount > 0) {
                countChosenAllow.setText("Bạn còn có thể chọn " + remainingCount + " vị trí.");
            } else {
                countChosenAllow.setText("Bạn đã chọn tối đa.");
            }
        } else {
            countChosenAllow.setText("Bạn có thể lựa chọn tối đa 5 vị trí.");
        }
    }

    private void getJobs() {
        Call<ArrayList<JobSearch>> call = jobSearchAPI.getListJobs();
        call.enqueue(new Callback<ArrayList<JobSearch>>() {
            @Override
            public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
                if (response.isSuccessful()) {
                    majors = response.body();
                    registerMajorAdapter.updateData(majors); // Cập nhật dữ liệu cho adapter
                    handleMajorAutoCheck();
                } else {
                    Log.d("testaaa", "onResponse: Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                Log.d("testaaa", "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleMajorClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            JobSearch clickedMajor = majors.get(position);
            if (!clickedMajor.getChecked()) {
                if (majorsChosen.size() < 5) {
                    majorsChosen.add(clickedMajor);
                    updateChosenCountTextView();
                } else {
                    Toast.makeText(RegisterMajorActivity.this, "Bạn đã chọn tối đa 5 vị trí.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                majorsChosen.remove(clickedMajor);
                updateChosenCountTextView();
            }
            clickedMajor.setChecked(!clickedMajor.getChecked());
            registerMajorAdapter.notifyItemChanged(position);
            majorChosenAdapter.notifyDataSetChanged();
            updateButtonBackground();
        }
    }

    private void handleChosenMajorClick(int position) {
        JobSearch major = majorsChosen.get(position);
        majorsChosen.remove(major);

        majorChosenAdapter.notifyDataSetChanged();
        updateChosenCountTextView();
        for (int i = 0; i < majors.size(); i++) {
            JobSearch originalMajor = majors.get(i);
            if (originalMajor.equals(major)) {
                originalMajor.setChecked(false);
                registerMajorAdapter.notifyItemChanged(i);
                break;
            }
        }
        updateButtonBackground();
    }

    // Lọc
    private void filter(String text) {
        ArrayList<JobSearch> filteredList = new ArrayList<>();

        for (JobSearch item : majors) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        registerMajorAdapter.updateData(filteredList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleMajorAutoCheck() {
        for (int position = 0; position < majors.size(); position++) {
            for (int check = 0; check < majorsChosen.size(); check++) {
                if (majors.get(position).getTitle().equals(majorsChosen.get(check).getTitle())){
                    majors.get(position).setChecked(true);
                }
            }
        }
        registerMajorAdapter.updateData(majors);
        registerMajorAdapter.notifyDataSetChanged();
        majorChosenAdapter.notifyDataSetChanged();
    }
}