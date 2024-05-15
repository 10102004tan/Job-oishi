package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Adapters.JobSearchAdapter;
import com.example.joboishi.Api.JobSearchAPI;
import com.example.joboishi.Models.Job;
import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.PaginationScrollListener;
import com.example.joboishi.databinding.SearchResultLayoutBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity {

    private SearchResultLayoutBinding binding;
    private TextInputEditText input;
    private ArrayList<JobSearch> listSearchJob = new ArrayList<>();
    private JobSearchAdapter adapter;
    private RecyclerView recyclerView;
    private JobSearchAPI jobSearchAPI;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int pageSize = 10;
    private RecyclerView rcv_tool;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchResultLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các thành phần và adapter
        recyclerView = binding.listJobSearched;
        ImageButton btnBack = binding.btnToolbarBack;
        TextInputLayout inputForm = binding.inputForm;
        rcv_tool = binding.listToolSearch;
        progressBar = binding.progressBar;

        btnBack.setOnClickListener(view -> finish());

        // Vô hiệu hóa sự kiện nhập liệu trên trường văn bản
        inputForm.getEditText().setFocusable(false);

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

        if (input.getText().length() > 0) {
            new Handler().postDelayed(() -> {
                jobSearchAPI.getListSearchJob(input.getText().toString(), currentPage, pageSize)
                        .enqueue(new Callback<ArrayList<JobSearch>>() {
                            @Override
                            public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
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
                                    if (response.body() == null || response.body().isEmpty()) {
                                        Toast.makeText(SearchResultActivity.this, "Không tìm thấy công việc nào", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                                adapter.removeFooterLoading();
                                isLoading = false;
                                Log.d("error", t.getMessage() + "");
                            }
                        });
            }, 2000);
        }
        else {
            new Handler().postDelayed(() -> jobSearchAPI.getListSearchJobAll(currentPage, pageSize).enqueue(new Callback<ArrayList<JobSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
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
                        if (response.body() == null || response.body().isEmpty()) {
                            Toast.makeText(SearchResultActivity.this, "Không tìm thấy công việc nào", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                    adapter.removeFooterLoading();
                    isLoading = false;
                    Log.d("error", t.getMessage() + "");
                }
            }), 2000);
        }

    }

    private void performJobSearch(String key, int page) {
        if (input.getText().length() > 0) {
            jobSearchAPI.getListSearchJob(
                    key,
                    page,
                    pageSize
            ).enqueue(new Callback<ArrayList<JobSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (page == 1) {
                            listSearchJob = response.body();
                            adapter = new JobSearchAdapter(listSearchJob, SearchResultActivity.this);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
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
                public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                    Log.d("error", t.getMessage() + "");
                }
            });
        }
        else {
            jobSearchAPI.getListSearchJobAll(
                    page,
                    pageSize
            ).enqueue(new Callback<ArrayList<JobSearch>>() {
                @Override
                public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (page == 1) {
                            listSearchJob = response.body();
                            adapter = new JobSearchAdapter(listSearchJob, SearchResultActivity.this);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
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
                public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                    Log.d("error", t.getMessage() + "");
                }
            });
        }
    }
}
