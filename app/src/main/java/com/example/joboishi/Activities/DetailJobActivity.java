package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.BenefitAdapter;
import com.example.joboishi.Api.DetailJobAPI;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.Models.Jobs;
import com.example.joboishi.R;
import com.example.joboishi.databinding.DetailJobLayoutBinding;
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

public class DetailJobActivity extends AppCompatActivity {

    private DetailJobLayoutBinding binding;
    private ArrayList<Job> jobs;
    private Job jobDetail;
    private Intent intent;
    private String jobId;
    private DetailJobAPI detailJobAPI;

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
        setContentView(R.layout.detail_job_layout);
        binding = DetailJobLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Dang ki receiver
        registerInternetBroadcastReceiver();

        // Show loading indicator initially
        showLoadingIndicator();

        // Lợi ích recycler view
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);


        //Lay du lieu tu job api
//        jobId = "2032881";
        jobId = getIntent().getIntExtra("JOB_ID", -1) + "";
        if (jobId.equals("-1")) {
            // Không tìm thấy JOB_ID, xử lý lỗi
            Toast.makeText(this, "Job ID not found!", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu không có ID hợp lệ
            return;
        }
        getDetailJob(jobId, new DetailJobCallback() {
            @Override
            public void onDetailJobLoaded(Job job) {
                // Hide loading indicator
                hideLoadingIndicator();

//                Log.d("test", job);

                //Responsibilities
                String longDescription = job.getResponsibilities();
                SpannableStringBuilder ssb = processStringWithBullet(longDescription.trim());
                binding.txtResponsibilities.setText(ssb);

                //Requirement
                SpannableStringBuilder requirement = processStringWithBullet(job.getRequirements().trim());
                binding.txtRequirements.setText(requirement);
                //Title job
                binding.lblJobTitle.setText(job.getTitle());
                //Logo company
                Glide.with(DetailJobActivity.this)
                        .load(job.getCompany().getImage_logo())
                        .into(binding.imgCompanyLogo)
                ;
                //Company name
                binding.txtCompanyName.setText(job.getCompany().getDisplay_name());
                //Job content
                binding.txtJobContent.setText(job.getContent());


                //Skills
                // Xóa tất cả các TextView hiện có từ LinearLayout
                binding.skillsLayout.removeAllViews();

                // Thêm các TextView mới
                for (String item : job.getSkills()) {
                    TextView textView = new TextView(DetailJobActivity.this);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    textView.setText(item);
                    textView.setBackgroundResource(R.drawable.text_badge);
                    textView.setPadding(15, 5, 15, 5); // Thêm padding nếu cần
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    params.setMargins(0, 0, 20, 0); // Khoảng cách giữa các TextView
                    textView.setLayoutParams(params);
                    binding.skillsLayout.addView(textView); // Thêm TextView vào LinearLayout
                }


                //Benefits
                if(job.getBenefit() != null && job.getBenefit().size() > 0) {
                    binding.getBenefits.setVisibility(View.VISIBLE);
                    binding.getBenefitsTitle.setVisibility(View.VISIBLE);
                BenefitAdapter benefitAdapter = new BenefitAdapter(DetailJobActivity.this,job.getBenefit());
                binding.benefitsList.setLayoutManager(layoutManager1);
                binding.benefitsList.setAdapter(benefitAdapter);
                }
                else {
                    binding.getBenefits.setVisibility(View.GONE);
                    binding.getBenefitsTitle.setVisibility(View.GONE);
                }

                //Get Detail Company's Job
                //Bat su kiem chuyen sanng chi tiet cong ty
                intent = new Intent(DetailJobActivity.this, DetailCompanyActivity.class);
                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("test", job.getCompany().getId() + "");
                        intent.putExtra("COMPANY_ID", job.getCompany().getId());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onDetailJobFailed(String errorMessage) {
                // Xử lý khi có lỗi xảy ra
            }
        });


        //lister swipe refresh layout
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


    //Ham gọi API
    private void getDetailJob(String jobId, DetailJobCallback callback) {
        //Tao doi tuong retrofit
//        Log.d("test", DetailJobAPI.BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DetailJobAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        detailJobAPI = retrofit.create(DetailJobAPI.class);
        Call<Job> call = detailJobAPI.getJobDetail(jobId);
        call.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if(response.isSuccessful()){
                    Job detailJob = response.body();
                    assert detailJob != null;
                    callback.onDetailJobLoaded(detailJob);
                } else {
                    callback.onDetailJobFailed("Failed to get job details");
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                callback.onDetailJobFailed("Failed to get job details: " + t.getMessage());
            }
        });
    }

    public interface DetailJobCallback {
        void onDetailJobLoaded(Job job);
        void onDetailJobFailed(String errorMessage);
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



    //Ham hien thi loading
    private void showLoadingIndicator() {
        // Show loading indicator (e.g., ProgressBar)
        binding.main.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    //Ham an loading
    private void hideLoadingIndicator() {
        // Hide loading indicator
        binding.main.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }


    // Ham dang ki receiver
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
                new AestheticDialog.Builder(DetailJobActivity.this, DialogStyle.CONNECTIFY, DialogType.ERROR)
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
                    MotionToast.Companion.createToast(DetailJobActivity.this, "😍",
                            "Kết nối mạng đã được khôi phục",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(DetailJobActivity.this, R.font.helvetica_regular));
                }
            }
        };
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }
}