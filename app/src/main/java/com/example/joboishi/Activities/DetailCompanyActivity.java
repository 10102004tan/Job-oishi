package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
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

import com.bumptech.glide.Glide;
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
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_layout);
        binding = CompanyLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        initData();
//        JobAdapter adapter = new JobAdapter(jobs);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.jobsOfCompany.setLayoutManager(layoutManager);
//        binding.jobsOfCompany.setAdapter(adapter);

        company_id = getIntent().getIntExtra("COMPANY_ID", -1) + "";
        getDetailCompany(company_id,  new DetailCompanyCallBack() {
            @Override
            public void onGetCompanyLoaded(Company company) {
                Log.d("test", "This is company: " + company.getDisplay_name());
                //Company name
                binding.lblCompanyName.setText(company.getDisplay_name());
                //Company Address
                binding.lblLocation.setText(company.getAddress().get(0).getProvince());
                //Company size
                binding.lblQuantityPeople.setText(company.getCompany_size());

                //Company  logo
                Glide.with(DetailCompanyActivity.this)
                        .load(company.getImage_logo())
                        .into(binding.myImageView)
                ;

//               //Get About Company
                binding.lblAboutCompany.setText(company.getDisplay_name());

                //Get Company description
                SpannableStringBuilder description = processStringWithBullet(company.getDescription().trim());
                binding.lblDesctiption.setText(description);


                //Get Office Address
                String addressString = "";
                for (Address address: company.getAddress()) {
                    addressString += address.getStreet() + ", " + address.getDistrict() + ", " + address.getProvince() + "\n";
                }
                SpannableStringBuilder spannableStringBuilder = processStringWithBullet(addressString.trim());
                binding.lblAddresses.setText(spannableStringBuilder);

                //Get website
                binding.lblWebsite.setText(company.getWebsite());

            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                if (statusInternet == STATUS_NO_INTERNET){
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                if (response.isSuccessful()){
                    Company company = response.body();
                    assert company != null;
                    callBack.onGetCompanyLoaded(company);

                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {

            }
        });

    }

    //GetCompanyCallBack
    private interface DetailCompanyCallBack {
        void onGetCompanyLoaded(Company company);
    }

    //Ham xu ly chuoi thanh cac dau cham dau dong
    private SpannableStringBuilder processStringWithBullet(String longDescription){
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
                }
                else{
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

        Log.d("testtttt","tetssss");
    }

}