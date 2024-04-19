package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;

import com.example.joboishi.R;
import com.example.joboishi.databinding.DetailJobLayoutBinding;

public class DetailJobActivity extends AppCompatActivity {

    private DetailJobLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_layout);
//        binding = DetailJobLayoutBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        String longDescription = "Develop, code, test, and deploy new features with a primary focus on Golang (80%) and Python (20%), ensuring adherence to API standards, extensibility, robustness, and optimal performance.\n" +
//                "Maintain and enhance our high-performance architecture, creating scalable and testable components.\n" +
//                "Collaborate cross-functionally with Product and Engineering teams to help define the product and system design.\n" +
//                "Contribute to the development of APIs, and microservices, and potentially involved in scripting tasks.\n";
//
//        String arr[] = longDescription.split("\n");
//        //lưu khoảng cách giữa ký tự đầu dòng và nội dung
//        int bulletGap = (int) dp(15);
//
//        SpannableStringBuilder ssb = new SpannableStringBuilder();
//        for (int i = 0; i < arr.length; i++) {
//            String line = arr[i];
//            SpannableString ss = new SpannableString(line);
//            ss.setSpan(new BulletSpan(bulletGap), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ssb.append(ss);
//
//            //avoid last "\n"
//            if(i+1<arr.length)
//                ssb.append("\n");
//
//        }
//        binding.txtResponsibilities.setText(ssb);
    }
    private float dp(int dp) {
        return getResources().getDisplayMetrics().density * dp;
    }
}