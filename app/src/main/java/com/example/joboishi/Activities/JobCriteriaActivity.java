package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.SelectSalaryFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ActivityJobCriteriaBinding;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import org.w3c.dom.Text;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class JobCriteriaActivity extends AppCompatActivity {

    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private final SelectSalaryFragment salaryFragment = new SelectSalaryFragment();
    private InternetBroadcastReceiver internetBroadcastReceiver;
    private IntentFilter intentFilter;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private boolean isFirst = true;
    private ActivityJobCriteriaBinding binding;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobCriteriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //register broadcast receiver
        registerInternetBroadcastReceiver();


        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.job_criteria_toolbar_title);

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Salary Textview
        TextView salaryTextView = findViewById(R.id.salary_text_view);
        salaryTextView.setText("1 - 9 Tr");
        salaryFragment.setSalaryTextView(salaryTextView);
        salaryFragment.setMinSalarySelected(1);
        salaryFragment.setMaxSalarySelected(9);


        // Button choose salary
        LinearLayout buttonChooseSalary = findViewById(R.id.btn_choose_salary);
        buttonChooseSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
                myBottomSheetDialogFragment.setFragment(salaryFragment);
                myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");
            }
        });


        // listener swipe refresh layout
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
                new AestheticDialog.Builder(JobCriteriaActivity.this, DialogStyle.CONNECTIFY, DialogType.ERROR)
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
                    MotionToast.Companion.createToast(JobCriteriaActivity.this, "😍",
                            "Kết nối mạng đã được khôi phục",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobCriteriaActivity.this, R.font.helvetica_regular));
                }
            }
        };
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }
}