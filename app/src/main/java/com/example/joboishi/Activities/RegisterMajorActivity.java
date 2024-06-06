package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.RegisterMajorAdapter;
import com.example.joboishi.Adapters.SelectedJobAdapter;
import com.example.joboishi.Api.KeywordAPI;
import com.example.joboishi.Models.Keyword;
import com.example.joboishi.R;
import com.example.joboishi.databinding.RegisterMajorLayoutBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMajorActivity extends AppCompatActivity {
    private final ArrayList<String> majorsChosen = new ArrayList<>();

    private RegisterMajorLayoutBinding registerMajorLayoutBinding;
    private ArrayList<Keyword> majors = new ArrayList<>();
    private RegisterMajorAdapter registerMajorAdapter;
    private SelectedJobAdapter majorChosenAdapter;
    private KeywordAPI keywordAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMajorLayoutBinding = RegisterMajorLayoutBinding.inflate(getLayoutInflater());
        setContentView(registerMajorLayoutBinding.getRoot());

        // Get Views
        AppCompatButton btnNext = registerMajorLayoutBinding.btnNextAdress;
        TextView toolbarTitle = findViewById(R.id.toolbar_text_title);
        ImageButton btnBackToolBar = findViewById(R.id.btn_toolbar_back);

        // Get data from intent, from Job criteria Activity
        Intent intent = getIntent();
        String caller = intent.getStringExtra("caller");
        assert caller != null;
        if (caller.equals("JobCriteriaActivity")) {
            btnNext.setText(R.string.btn_save_label);
            ArrayList<String> major = (ArrayList<String>) intent.getSerializableExtra("majors");
            assert major != null;
            if (!major.isEmpty()) {
                majorsChosen.clear();
                majorsChosen.addAll(major);
            }
        } else if (caller.equals("LoginEmailActivity")) {
            btnNext.setText(R.string.continute);
            toolbarTitle.setVisibility(View.GONE);
            btnBackToolBar.setVisibility(View.GONE);
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

        // Initialize Retrofit and KeywordAPI
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KeywordAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        keywordAPI = retrofit.create(KeywordAPI.class);

        // Initialize adapter and set itemClickListener
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

        getJobs();

        majorChosenAdapter = new SelectedJobAdapter(this, majorsChosen);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        registerMajorLayoutBinding.showListMajorsChosen.setLayoutManager(layoutManager2);
        registerMajorLayoutBinding.showListMajorsChosen.setAdapter(majorChosenAdapter);
        majorChosenAdapter.setItemClickListener(new SelectedJobAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SelectedJobAdapter.MyViewHolder holder, int position) {
                handleChosenMajorClick(position);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasMajorsChosen()) {
                    if (caller.equals("JobCriteriaActivity")) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("majorsChosen", majorsChosen);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else if (caller.equals("LoginEmailActivity")) {
                        Intent intent = new Intent(RegisterMajorActivity.this, AddressActivity.class);
                        intent.putExtra("majorsChosen", majorsChosen);
                        intent.putExtra("caller", "RegisterMajorActivity");
                        intent.putExtra("is_first_login", true);
                        startActivity(intent);
                    }
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

    @SuppressLint("SetTextI18n")
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
        Call<ArrayList<Keyword>> call = keywordAPI.getListKeyword();
        call.enqueue(new Callback<ArrayList<Keyword>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Keyword>> call, @NonNull Response<ArrayList<Keyword>> response) {
                if (response.isSuccessful()) {
                    majors = response.body();
                    registerMajorAdapter.updateData(majors);
                    handleMajorAutoCheck();
                } else {
                    Log.d("testaaa", "onResponse: Unsuccessful response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Keyword>> call, @NonNull Throwable t) {
                Log.d("testaaa", "onFailure: " + t.getMessage());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleMajorClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Keyword clickedMajor = majors.get(position);
            if (!clickedMajor.getChecked()) {
                if (majorsChosen.size() < 5) {
                    majorsChosen.add(clickedMajor.getKeyword());
                    updateChosenCountTextView();
                } else {
                    Toast.makeText(RegisterMajorActivity.this, "Bạn đã chọn tối đa 5 vị trí.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                majorsChosen.remove(clickedMajor.getKeyword());
                updateChosenCountTextView();
            }
            clickedMajor.setChecked(!clickedMajor.getChecked());
            registerMajorAdapter.notifyItemChanged(position);
            majorChosenAdapter.notifyDataSetChanged();
            updateButtonBackground();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleChosenMajorClick(int position) {
        String major = majorsChosen.get(position);
        majorsChosen.remove(major);
        majorChosenAdapter.notifyDataSetChanged();
        updateChosenCountTextView();
        for (int i = 0; i < majors.size(); i++) {
            Keyword originalMajor = majors.get(i);
            if (originalMajor.getKeyword().equals(major)) {
                originalMajor.setChecked(false);
                registerMajorAdapter.notifyItemChanged(i);
                break;
            }
        }
        updateButtonBackground();
    }

    private void filter(String text) {
        ArrayList<Keyword> filteredList = new ArrayList<>();
        for (Keyword item : majors) {
            if (item.getKeyword().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        registerMajorAdapter.updateData(filteredList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleMajorAutoCheck() {
        for (int position = 0; position < majors.size(); position++) {
            for (String chosen : majorsChosen) {
                if (majors.get(position).getKeyword().equals(chosen)) {
                    majors.get(position).setChecked(true);
                }
            }
        }
        registerMajorAdapter.updateData(majors);
        registerMajorAdapter.notifyDataSetChanged();
        majorChosenAdapter.notifyDataSetChanged();
    }
}