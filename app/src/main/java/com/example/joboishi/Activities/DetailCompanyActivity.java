package com.example.joboishi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.ImageDescCompanyAdapter;
import com.example.joboishi.Api.CompanyByJobAPI;
import com.example.joboishi.Models.data.Address;
import com.example.joboishi.Models.data.Company;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.R;
import com.example.joboishi.databinding.CompanyLayoutBinding;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailCompanyActivity extends AppCompatActivity {

    private CompanyLayoutBinding binding;
    private ArrayList<Job> jobs;
    private CompanyByJobAPI companyByJobAPI;
    String company_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_layout);
        binding = CompanyLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
//
//        binding.jobsOfCompany.setAdapter(adapter);

        company_id = getIntent().getIntExtra("COMPANY_ID", -1) + "";
        getDetailCompany(company_id,  new DetailCompanyCallBack() {
            @Override
            public void onGetCompanyLoaded(Company company) {
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


                //Get Galaries image
                if(company.getImage_galleries() != null && company.getImage_galleries().size() > 0) {
                    binding.imageGalleriesItem.setVisibility(View.VISIBLE);
                    ImageDescCompanyAdapter adapter = new ImageDescCompanyAdapter(company.getImage_galleries(), DetailCompanyActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(DetailCompanyActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    binding.gralariesImage.setLayoutManager(layoutManager);
                    binding.gralariesImage.setAdapter(adapter);
                }
                else {
                    binding.imageGalleriesItem.setVisibility(View.GONE);
                }


            }
        });
    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed(); // Xử lý sự kiện khi nhấn nút quay lại
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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



}