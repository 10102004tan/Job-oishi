package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.JobSearchAPI;
import com.example.joboishi.Models.Job;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.PaginationScrollListener;
import com.example.joboishi.databinding.SearchResultLayoutBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity {

    private SearchResultLayoutBinding binding;
    private EditText input;
    private ArrayList<Job> listSearchJob = new ArrayList<>();
    private JobAdapter adapter;
    private RecyclerView recyclerView;
    private JobSearchAPI jobSearchAPI;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchResultLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các thành phần và adapter
        recyclerView = binding.listJobSearched;
        ImageButton btnBack = binding.btnToolbarBack;
        btnBack.setOnClickListener(view -> finish());

        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            public void isLoadItem() {
                if (!isLoading && !isLastPage) {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPage();
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("key")) {
            String searchText = intent.getStringExtra("key");
            input = binding.inputMajor;
            input.setText(searchText);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JobSearchAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jobSearchAPI = retrofit.create(JobSearchAPI.class);
            performJobSearch(searchText, 1);
        }
    }

    private void loadNextPage() {
        adapter.addFooterLoading();

        new Handler().postDelayed(() -> jobSearchAPI.getListSearchJob(
                input.getText().toString(),
                currentPage,
                pageSize
        ).enqueue(new Callback<ArrayList<Job>>() {
            @Override
            public void onResponse(Call<ArrayList<Job>> call, Response<ArrayList<Job>> response) {
                adapter.removeFooterLoading();

                if (response.isSuccessful() && response.body() != null) {
                    listSearchJob.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    isLoading = false;

                    if (response.body().size() < pageSize) {
                        isLastPage = true;
                    }
                } else {
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Job>> call, Throwable t) {
                adapter.removeFooterLoading();
                isLoading = false;
                Log.d("error", t.getMessage() + "");
            }
        }), 2000);
    }

    private void performJobSearch(String key, int page) {
        jobSearchAPI.getListSearchJob(
                key,
                page,
                pageSize
        ).enqueue(new Callback<ArrayList<Job>>() {
            @Override
            public void onResponse(Call<ArrayList<Job>> call, Response<ArrayList<Job>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (page == 1) {
                        listSearchJob = response.body();
                        adapter = new JobAdapter(listSearchJob, SearchResultActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        listSearchJob.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    if (response.body().size() < pageSize) {
                        isLastPage = true;
                    }
                } else {
                    Toast.makeText(SearchResultActivity.this, "Không tìm thấy công việc nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Job>> call, Throwable t) {
                Log.d("error", t.getMessage() + "");
            }
        });
    }
}
