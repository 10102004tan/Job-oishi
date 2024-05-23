package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.ImageDescCompanyAdapter;
import com.example.joboishi.Api.CompanyByJobAPI;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Models.data.Address;
import com.example.joboishi.Models.data.Company;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.R;
import com.example.joboishi.databinding.CompanyLayoutBinding;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class DetailCompanyActivity extends AppCompatActivity {

    private CompanyLayoutBinding binding;
    private ArrayList<Job> jobs;
    private CompanyByJobAPI companyByJobAPI;
    String company_id;
    private InternetBroadcastReceiver internetBroadcastReceiver;
    private IntentFilter intentFilter;
    private final int STATUS_NO_INTERNET = 0;
    private final int STATUS_LOW_INTERNET = 1;
    private final int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_layout);
        binding = CompanyLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Shimmer layout
        binding.companyShimmer.startShimmerAnimation();
        binding.companyShimmer.setVisibility(View.VISIBLE);
        binding.companyLayout.setVisibility(View.INVISIBLE);

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText("Company");

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        initData();
//        JobAdapter adapter = new JobAdapter(jobs);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.jobsOfCompany.setLayoutManager(layoutManager);
//        binding.jobsOfCompany.setAdapter(adapter);

        company_id = getIntent().getIntExtra("COMPANY_ID", -1) + "";
//        company_id = "94691";
        Log.d("test", "Company id: " + company_id);

        getDetailCompany(company_id, new DetailCompanyCallBack() {
            @Override
            public void onGetCompanyLoaded(Company company) {
                //Show layout
                binding.companyShimmer.stopShimmerAnimation();
                binding.companyShimmer.setVisibility(View.GONE);
                binding.companyLayout.setVisibility(View.VISIBLE);

                // Company name
                if (company.getDisplay_name() != null && !company.getDisplay_name().isEmpty()) {
                    binding.lblCompanyName.setText(company.getDisplay_name());
                }

                // Company Address
                if (company.getAddress() != null && !company.getAddress().isEmpty()) {
                    binding.lblLocation.setText(company.getAddress().get(0).getProvince());
                }

                // Company size
                if (company.getCompany_size() != null && !company.getCompany_size().isEmpty()) {
                    binding.lblQuantityPeople.setText(company.getCompany_size());
                }

                // Company field
                StringBuilder industriesBuilder = new StringBuilder();
                if (company.getIndustries_arr() != null && !company.getIndustries_arr().isEmpty()) {
                    int industriesCount = company.getIndustries_arr().size();
                    // Iterate through the industries list
                    for (int i = 0; i < industriesCount; i++) {
                        String industry = company.getIndustries_arr().get(i);
                        if (i > 0) {
                            industriesBuilder.append(", ");
                        }
                        industriesBuilder.append(industry);

                        // If there are more than 3 elements, break the loop after the third element
                        if (i == 2) {
                            industriesBuilder.append("...");
                            break;
                        }
                    }
                }
                // Convert the StringBuilder to a String
                String industries = industriesBuilder.toString();
                // Set the text to the label
                binding.lblField.setText(industries);

                // Company logo
                if (company.getImage_logo() != null && !company.getImage_logo().isEmpty()) {
                    Glide.with(DetailCompanyActivity.this)
                            .load(company.getImage_logo())
                            .into(binding.myImageView);
                }

                // Get About Company
                if (company.getDisplay_name() != null && !company.getDisplay_name().isEmpty()) {
                    binding.lblAboutCompany.setText(company.getDisplay_name());
                }

                // Get Company description
                if (company.getDescription() != null && !company.getDescription().trim().isEmpty()) {
                    SpannableStringBuilder description = processStringWithBullet(company.getDescription().trim());
                    binding.lblDesctiption.setText(description);
                }


                // Get Office Address
                if (company.getAddress() != null && !company.getAddress().isEmpty()) {
                    StringBuilder addressStringBuilder = new StringBuilder();
                    for (Address address : company.getAddress()) {
                        if (address.getStreet() != null && address.getDistrict() != null && address.getProvince() != null) {
                            addressStringBuilder.append(address.getStreet())
                                    .append(", ")
                                    .append(address.getDistrict())
                                    .append(", ")
                                    .append(address.getProvince())
                                    .append("\n");
                        }
                    }
                    String addressString = addressStringBuilder.toString().trim();
                    if (!addressString.isEmpty()) {
                        SpannableStringBuilder spannableStringBuilder = processStringWithBullet(addressString);
                        binding.lblAddresses.setText(spannableStringBuilder);
                    }
                }

                // Get website
                if (company.getWebsite() != null && !company.getWebsite().isEmpty()) {
                    binding.lblWebsite.setText(company.getWebsite());
                }

                // Get Galleries image
                if (company.getImage_galleries() != null && !company.getImage_galleries().isEmpty()) {
                    binding.imageGalleriesItem.setVisibility(View.VISIBLE);
                    ImageDescCompanyAdapter adapter = new ImageDescCompanyAdapter(company.getImage_galleries(), DetailCompanyActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(DetailCompanyActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    binding.gralariesImage.setLayoutManager(layoutManager);
                    binding.gralariesImage.setAdapter(adapter);
                } else {
                    binding.imageGalleriesItem.setVisibility(View.GONE);
                }


            }
        });

//        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (statusPreInternet != statusInternet) {
//                    registerInternetBroadcastReceiver();
//                    isFirst = true;
//                }
//                if (statusInternet == STATUS_NO_INTERNET) {
//                    binding.swipeRefreshLayout.setRefreshing(false);
//                }
//                binding.swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    //Get Company
    private void getDetailCompany(String company_id, DetailCompanyCallBack callBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CompanyByJobAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        companyByJobAPI = retrofit.create(CompanyByJobAPI.class);
        Call<Company> call = companyByJobAPI.getCompanyByJob(company_id);
        call.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                if (response.isSuccessful()) {
                    Company company = response.body();
                    assert company != null;
                    callBack.onGetCompanyLoaded(company);

                }
                else {
                    binding.lblNotFound.setVisibility(View.VISIBLE);
                    Log.d("test", "Lỗi không thể phản hồi công ty này ");
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                binding.lblNotFound.setVisibility(View.VISIBLE);
                Log.d("test", "Lỗi không thể đọc công ty này ");
            }
        });

    }

    //GetCompanyCallBack
    private interface DetailCompanyCallBack {
        void onGetCompanyLoaded(Company company);
    }

    //Ham xu ly chuoi thanh cac dau cham dau dong
    private SpannableStringBuilder processStringWithBullet(String longDescription) {
        String arr[] = longDescription.split("\n");
        //lưu khoảng cách giữa ký tự đầu dòng và nội dung
        int bulletGap = (int) getResources().getDisplayMetrics().density * 15;

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String line = arr[i];
            SpannableString ss = new SpannableString(line);
            ss.setSpan(new BulletSpan(bulletGap), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);

            // Tránh thêm "\n" cuối cùng
            if (i + 1 < arr.length)
                ssb.append("\n");

            // Thêm khoảng trống sau mỗi đoạn văn
            ssb.append("\n");

        }
        return ssb;
    }

    private void registerInternetBroadcastReceiver() {
        internetBroadcastReceiver = new InternetBroadcastReceiver();
        internetBroadcastReceiver.listener = new InternetBroadcastReceiver.IInternetBroadcastReceiverListener() {
            @Override
            public void noInternet() {
                statusPreInternet = STATUS_NO_INTERNET;
                if (isFirst) {
                    binding.main.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                    binding.image.setAnimation(R.raw.a404);
                    binding.image.playAnimation();
                    statusInternet = STATUS_NO_INTERNET;
                    binding.swipeRefreshLayout.setRefreshing(false);
                    isFirst = false;

                }
                new AestheticDialog.Builder(DetailCompanyActivity.this, DialogStyle.CONNECTIFY, DialogType.ERROR)
                        .setTitle("Không có kết nối mạng")
                        .setMessage("Vui lòng kiểm tra lại kết nối mạng")
                        .setCancelable(false)
                        .setGravity(Gravity.BOTTOM).show();
            }

            @Override
            public void lowInternet() {
                binding.image.setVisibility(View.VISIBLE);
                binding.main.setVisibility(View.GONE);
            }

            @Override
            public void goodInternet() {
                statusPreInternet = STATUS_GOOD_INTERNET;
                if (isFirst) {
                    statusInternet = STATUS_GOOD_INTERNET;
                    isFirst = false;
                } else {
                    binding.image.setVisibility(View.GONE);
                    binding.main.setVisibility(View.VISIBLE);
                    MotionToast.Companion.createToast(DetailCompanyActivity.this, "😍",
                            "Kết nối mạng đã được khôi phục",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(DetailCompanyActivity.this, R.font.helvetica_regular));
                }
            }
        };
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);

        Log.d("testtttt", "tetssss");
    }

}