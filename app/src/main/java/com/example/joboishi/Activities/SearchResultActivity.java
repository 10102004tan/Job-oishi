package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.joboishi.Adapters.JobSearchAdapter;
import com.example.joboishi.Adapters.SearchOptionAdapter;
import com.example.joboishi.Api.JobSearchAPI;
import com.example.joboishi.Fragments.BottomSheetDialog.OptionExperienceFragment;
import com.example.joboishi.Fragments.BottomSheetDialog.OptionTypeJobFragment;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.PaginationScrollListener;
import com.example.joboishi.databinding.SearchResultLayoutBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vdx.designertoast.DesignerToast;

import java.util.ArrayList;
import java.util.Arrays;

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
    private SearchOptionAdapter optionAdapter;
    private RecyclerView recyclerView;
    private JobSearchAPI jobSearchAPI;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int pageSize = 10;
    private RecyclerView rcv_tool;

    private LottieAnimationView progressBar;
    private LottieAnimationView animateNoData;

    MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchResultLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các thành phần và adapter
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

        optionAdapter.setOnItemClickListener(new SearchOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        DesignerToast.Success(SearchResultActivity.this, position + "", Gravity.CENTER, Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        listSearchJob.clear();
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.VISIBLE);
                        currentPage = 1; // Reset current page when performing new search
                        isLastPage = false;
                        performJobSearchRm(input.getText().toString(), currentPage);
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
            performJobSearch(searchText, 1);
        }
    }

    private void loadNextPage() {
        adapter.addFooterLoading();
        jobSearchAPI.getListSearchJob(input.getText().toString(), currentPage, pageSize)
                .enqueue(new Callback<ArrayList<JobSearch>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
                        adapter.removeFooterLoading();
                        isLoading = false;

                        if (response.isSuccessful() && response.body() != null) {
                            listSearchJob.addAll(response.body());
                            adapter.notifyDataSetChanged();

                            if (response.body().size() < pageSize) {
                                isLastPage = true;
                            }
                        } else {
                            if (listSearchJob.isEmpty()) {
                                showNoDataAnimation();
                            } else {
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
    }

    private void performJobSearch(String key, int page) {
        progressBar.setVisibility(View.VISIBLE);
        jobSearchAPI.getListSearchJob(key, page, pageSize)
                .enqueue(new Callback<ArrayList<JobSearch>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            if (page == 1) {
                                listSearchJob.clear();
                            }
                            listSearchJob.addAll(response.body());
                            if (listSearchJob.isEmpty()) {
                                showNoDataAnimation();
                            } else {
                                hideNoDataAnimation();
                                adapter = new JobSearchAdapter(listSearchJob, SearchResultActivity.this);
                                recyclerView.setAdapter(adapter);
                            }

                            if (response.body().size() < pageSize) {
                                isLastPage = true;
                            } else {
                                isLastPage = false;
                            }
                        } else {
                            showNoDataAnimation();
                            Toast.makeText(SearchResultActivity.this, "Không tìm thấy công việc nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("error", t.getMessage() + "");
                    }
                });
    }

    private void performJobSearchRm(String key, int page) {
        progressBar.setVisibility(View.VISIBLE);
        jobSearchAPI.getListSearchRmJob(key, page, pageSize)
                .enqueue(new Callback<ArrayList<JobSearch>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JobSearch>> call, Response<ArrayList<JobSearch>> response) {
                        progressBar.setVisibility(View.GONE);
                        isLoading = false;

                        if (response.isSuccessful() && response.body() != null) {
                            if (page == 1) {
                                listSearchJob.clear();
                            }
                            listSearchJob.addAll(response.body());
                            if (listSearchJob.isEmpty()) {
                                showNoDataAnimation();
                            } else {
                                hideNoDataAnimation();
                                adapter.notifyDataSetChanged();
                            }

                            if (response.body().size() < pageSize) {
                                isLastPage = true;
                            } else {
                                isLastPage = false;
                            }
                        } else {
                            showNoDataAnimation();
                            Toast.makeText(SearchResultActivity.this, "Không tìm thấy công việc nào", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JobSearch>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        isLoading = false;
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
    }
}
