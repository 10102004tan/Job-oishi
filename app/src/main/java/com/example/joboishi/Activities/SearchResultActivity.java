package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.joboishi.Adapters.JobSearchAdapter;
import com.example.joboishi.Adapters.OptionExperienceAdapter;
import com.example.joboishi.Adapters.OptionJobTypeAdapter;
import com.example.joboishi.Adapters.SearchOptionAdapter;
import com.example.joboishi.Api.JobSearchAPI;
import com.example.joboishi.Fragments.BottomSheetDialog.OptionExperienceFragment;
import com.example.joboishi.Fragments.BottomSheetDialog.OptionTypeJobFragment;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.Abstracts.PaginationScrollListener;
import com.example.joboishi.databinding.SearchResultLayoutBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity implements OptionTypeJobFragment.OnOptionSelectedListener, OptionExperienceFragment.OnOptionSelectedListener {

    private SearchResultLayoutBinding binding;
    private TextInputEditText input;
    private ArrayList<JobBasic> listSearchJob = new ArrayList<>();
    private JobSearchAdapter adapter;
    private SearchOptionAdapter optionAdapter;
    private RecyclerView recyclerView;
    private JobSearchAPI jobSearchAPI;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static int currentPage = 1;
    private int pageSize = 10;
    private RecyclerView rcv_tool;

    private LottieAnimationView progressBar;
    private LottieAnimationView animateNoData;

    private Boolean isRemote = false;
    private static String jobType;
    private static String experience;

    MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchResultLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize components and adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JobSearchAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jobSearchAPI = retrofit.create(JobSearchAPI.class);

        recyclerView = binding.listJobSearched;
        ImageButton btnBack = binding.btnToolbarBack;
        TextInputLayout inputForm = binding.inputForm;
        rcv_tool = binding.listToolSearch;
        progressBar = binding.progressBar;
        animateNoData = binding.animateNoData;
        input = binding.inputMajor;
        Intent intent = getIntent();
        input.setText(intent.getStringExtra("key"));
        String kword = "";
        if (input.getText().length() > 0) {
            kword = input.getText().toString();
        }

        adapter = new JobSearchAdapter(listSearchJob, SearchResultActivity.this);
        adapter.setiClickViewDetail(new JobSearchAdapter.iClickItem() {
            @Override
            public void clickViewDetail(int id) {
                Intent intent = new Intent(SearchResultActivity.this, DetailJobActivity.class);
                intent.putExtra("JOB_ID", id);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        // Set the initial visibility
        animateNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        btnBack.setOnClickListener(view -> finish());

        // Option Tool
        ArrayList<String> listOption = new ArrayList<>(Arrays.asList("Mới đăng tuyển", "Làm từ xa", "Năm kinh nghiệm", "Loại công việc"));

        optionAdapter = new SearchOptionAdapter(SearchResultActivity.this, listOption);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchResultActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcv_tool.setLayoutManager(linearLayoutManager);
        rcv_tool.setAdapter(optionAdapter);

        String finalKword = kword;
        optionAdapter.setOnItemClickListener(new SearchOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        // Handle "Mới đăng tuyển" option
                        break;
                    case 1:
                        isRemote = !isRemote;
                        if (listSearchJob.size() > 0) {
                            listSearchJob.clear();
                            adapter.notifyDataSetChanged();
                        }
                        currentPage = 1;
                        progressBar.setVisibility(View.VISIBLE);
                        performJobSearch(finalKword, currentPage);
                        break;
                    case 2:
                        dialogFragment.setFragment(new OptionExperienceFragment());
                        dialogFragment.show(getSupportFragmentManager(), "experience");
                        break;
                    case 3:
                        dialogFragment.setFragment(new OptionTypeJobFragment());
                        dialogFragment.show(getSupportFragmentManager(), "type job");
                        break;
                }
            }
        });

        // Disable text input
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

        performJobSearch(input.getText().toString(), 1);
    }

    private void loadNextPage() {
        adapter.addFooterLoading();
        jobSearchAPI.getListSearchJob(input.getText().toString(), isRemote, experience, jobType, currentPage, pageSize)
                .enqueue(new Callback<ArrayList<JobBasic>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
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
                            if (listSearchJob.isEmpty()) {
                                showNoDataAnimation();
                            } else {
                                Toast.makeText(SearchResultActivity.this, "Không tìm thấy công việc nào", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                        adapter.removeFooterLoading();
                        isLoading = false;
                        Log.d("error", t.getMessage() + "");
                    }
                });
    }

    private void performJobSearch(String key, int page) {
        progressBar.setVisibility(View.VISIBLE);
        jobSearchAPI.getListSearchJob(key, isRemote, experience, jobType, page, pageSize).enqueue(new Callback<ArrayList<JobBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (page == 1) {
                        listSearchJob.clear();
                        listSearchJob.addAll(response.body());
                        if (listSearchJob.isEmpty()) {
                            showNoDataAnimation();
                        } else {
                            hideNoDataAnimation();
                            adapter.setData(listSearchJob);
                        }
                    } else {
                        listSearchJob.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                    if (response.body().size() < pageSize) {
                        isLastPage = true;
                    }
                } else {
                    showNoDataAnimation();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("error", t.getMessage() + "");
            }
        });
    }

    private void showNoDataAnimation() {
        animateNoData.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void hideNoDataAnimation() {
        animateNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onOptionSelected(String selectedOption, int pos) {
        if (selectedOption == null) {
            selectedOption = "";
        }
        switch (pos) {
            case 0:
                jobType = selectedOption.isEmpty() ? "" : selectedOption.toLowerCase();
                break;
            case 1:
                experience = selectedOption.isEmpty() ? "" : selectedOption.toLowerCase();
                break;
        }
        if (listSearchJob.size() > 0) {
            listSearchJob.clear();
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.VISIBLE);
        performJobSearch(input.getText().toString(), 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        jobType = "";
        experience = "";
    }

}
