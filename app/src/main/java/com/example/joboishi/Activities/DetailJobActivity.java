package com.example.joboishi.Activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joboishi.Adapters.BenefitAdapter;
import com.example.joboishi.Api.DetailJobAPI;
import com.example.joboishi.Api.JobAppliedAPI;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.R;
import com.example.joboishi.Abstracts.BaseActivity;
import com.example.joboishi.databinding.DetailJobLayoutBinding;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailJobActivity extends BaseActivity {

    private DetailJobLayoutBinding binding;
    private ArrayList<Job> jobs;
    private Job jobDetail;
    private Intent intent;
    private String jobId;
    private DetailJobAPI detailJobAPI;
    private ProgressDialog progressDialog;
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

        //Init progress dialog
        progressDialog = new ProgressDialog(DetailJobActivity.this);
        progressDialog.setMessage("Please waite...");

        //Show shimmer
        binding.shimmer.startShimmerAnimation();
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.detailLayout.setVisibility(View.INVISIBLE);


        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText("");

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Benefits recycler view
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);


        //Lay du lieu tu job api
        //jobId = "2032881";
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
                binding.shimmer.stopShimmerAnimation();
                binding.shimmer.setVisibility(View.GONE);
                binding.detailLayout.setVisibility(View.VISIBLE);

//                Log.d("test", job.getContent());

                // Responsibilities
                if (job.getResponsibilities() != null) {
                    String longDescription = job.getResponsibilities().trim();
                    SpannableStringBuilder ssb = processStringWithBullet(longDescription);
                    binding.txtResponsibilities.setText(ssb);
                }

                // Requirements
                if (job.getRequirements() != null) {
                    SpannableStringBuilder requirement = processStringWithBullet(job.getRequirements().trim());
                    binding.txtRequirements.setText(requirement);
                }

                // Job Title
                if (job.getTitle() != null) {
                    binding.lblJobTitle.setText(job.getTitle());
                }

                // Company Logo
                if (job.getCompany().getImage_logo() != null) {
                    Glide.with(DetailJobActivity.this)
                            .load(job.getCompany().getImage_logo())
                            .into(binding.imgCompanyLogo);
                }

                // Company Name
                if (job.getCompany() != null && job.getCompany().getDisplay_name() != null) {
                    binding.txtCompanyName.setText(job.getCompany().getDisplay_name());
                }

                // Job Salary
                    if (!job.getIs_salary_visible()) {
                        binding.txtSalary.setText("Công ty bảo mật thông tin này");
                    } else {
                        binding.txtSalary.setText(job.getSalary_value());
                    }

                // Company Location
                if (job.getCompany() != null && !job.getCompany().getAddress().isEmpty()) {
                    String district = job.getCompany().getAddress().get(0).getDistrict();
                    String province = job.getCompany().getAddress().get(0).getProvince();
                    if (district != null && province != null) {
                        binding.txtLocation.setText(district + ", " + province);
                    }
                }

                // Job Content
                if (job.getContent() != null) {
                    binding.txtJobContent.setText(job.getContent().trim());
                }

                // Skills
                // Xóa tất cả các TextView hiện có từ LinearLayout
                binding.skillsLayout.removeAllViews();
                if (job.getSkills() != null && !job.getSkills().isEmpty()) {
                    // Thêm các TextView mới
                    for (String item : job.getSkills()) {
                        if (item != null) {
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
                    }
                }

                // Benefits
                if (job.getBenefit() != null && !job.getBenefit().isEmpty()) {
                    binding.getBenefits.setVisibility(View.VISIBLE);
                    binding.getBenefitsTitle.setVisibility(View.VISIBLE);
                    BenefitAdapter benefitAdapter = new BenefitAdapter(DetailJobActivity.this, job.getBenefit());
                    binding.benefitsList.setLayoutManager(layoutManager1);
                    binding.benefitsList.setAdapter(benefitAdapter);
                } else {
                    binding.getBenefits.setVisibility(View.GONE);
                    binding.getBenefitsTitle.setVisibility(View.GONE);
                }

                // Get Company logo
                if (job.getCompany() != null) {
                    if (job.getCompany().getDisplay_name() != null) {
                        binding.companyName.setText(job.getCompany().getDisplay_name());
                    }
                    if (job.getCompany().getCompany_size() != null) {
                        binding.txtCompanySize.setText(job.getCompany().getCompany_size());
                    }
                    if (job.getCompany().getImage_logo() != null) {
                        Glide.with(DetailJobActivity.this)
                                .load(job.getCompany().getImage_logo())
                                .into(binding.avatarCompany);
                    }
                }

                //Get Detail Company's Job
                //Bat su kiem chuyen sanng chi tiet cong ty
                intent = new Intent(DetailJobActivity.this, DetailCompanyActivity.class);
                intent.putExtra("COMPANY_ID", job.getCompany().getId());
                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("test", job.getCompany().getId() + "");
                        startActivity(intent);
                    }
                });

                binding.txtCompanyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(intent);
                    }
                });

                binding.imgCompanyLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                });

                //Applied Job Event From Serve

                if (job.isIs_edit()) {
                    binding.btnApplied.setEnabled(true);
                }
                else {
                    binding.btnApplied.setEnabled(false);
                }
                binding.btnApplied.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppliedJob(
                                job.getId() + "",
                                "7"
                        );
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
        Log.d("test", DetailJobAPI.BASE_URL);
        Log.d("test", jobId);
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
                    Log.d("test", "onResponse: " + response.body().getContent());
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

    //Ham Applied Job
    private void AppliedJob (String job_id, String user_id) {
        //Tao Retrofit
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JobAppliedAPI.BASE_URL)
                .build();
        JobAppliedAPI jobAppliedAPI = retrofit.create(JobAppliedAPI.class);

        //Chuyển đổi các tham số thành RequestBody
        RequestBody job_idRequestBody = RequestBody.create(MediaType.parse("text/plain"), job_id);
        RequestBody user_idRequestBody = RequestBody.create(MediaType.parse("text/plain"), user_id);

        //Gửi yêu cầu tải lên
        Call<ResponseBody> call = jobAppliedAPI.applied(
                job_idRequestBody, user_idRequestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
//                    Log.d("test", "Response");
                    progressDialog.dismiss();
                    showDialog("Ứng tuyển thành công", true);
                }
                else {
                    progressDialog.dismiss();
                    showDialog("Ứng tuyển không thành công", false);
                    Log.d("test", "Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                showDialog("Ứng tuyển không thành công", false);
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






    @Override
    protected void handleNoInternet() {
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
    protected void handleLowInternet() {
        binding.image.setVisibility(View.VISIBLE);
        binding.main.setVisibility(View.GONE);
    }

    @Override
    protected void handleGoodInternet() {
        statusPreInternet = STATUS_GOOD_INTERNET;
        if (isFirst) {
            statusInternet = STATUS_GOOD_INTERNET;
            isFirst = false;
        }
        else{
            binding.image.setVisibility(View.GONE);
            binding.main.setVisibility(View.VISIBLE);
        }
    }

    //Show dialog
    private void showDialog(String message, Boolean type) {
        //Create the Dialog here
        Dialog dialog = new Dialog(DetailJobActivity.this);
        dialog.setContentView(R.layout.custom_dialog_layout);

        TextView lbl_title = dialog.findViewById(R.id.textView2);
        lbl_title.setText(message);
        if(type) {
            ImageView imageView = dialog.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_checked);
            TextView lbl_Name = dialog.findViewById(R.id.textView);
            lbl_Name.setText("Successful");
        }
        else {
            ImageView imageView = dialog.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_failed);
            TextView lbl_Name = dialog.findViewById(R.id.textView);
            lbl_Name.setText("Failed");
        }


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        dialog.show();

        Button Okay = dialog.findViewById(R.id.btn_okay);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}