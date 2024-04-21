package com.example.joboishi.Activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;

import com.example.joboishi.Fragments.BottomSheetDialog.ChooseLanguageDisplayFragment;
import com.example.joboishi.Fragments.BottomSheetDialog.FilterJobFragment;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.R;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.joboishi.databinding.SettingLayoutBinding;

public class SettingActivity extends AppCompatActivity {
    private SettingLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SettingLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView btnAccount = binding.btnChangeAccount;
        TextView btnThongBao = binding.btnThongBao;
        LinearLayout btnGopY = binding.btnGopY;
        LinearLayout btnHoTro = binding.btnSupport;
        LinearLayout btnBaoMat = binding.btnSecurity;
        LinearLayout btnDieuKhoanSuDung = binding.dieuKhoanSuDung;
        LinearLayout btnLanguage = binding.btnLanguage;
        AppCompatButton btnLogOut = binding.btnLogout;

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AccountSettingActivity.class);
                // Chuyển đến NextActivity
                startActivity(intent);
            }
        });

        btnThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        btnGopY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        btnBaoMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        btnDieuKhoanSuDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();
                dialogFragment.setFragment(new ChooseLanguageDisplayFragment());
                dialogFragment.show(getSupportFragmentManager(), "choose language");
            }
        });

        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.button_background_change);
        animatorSet.setTarget(btnLogOut);
        btnLogOut.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btnLogOut.setBackgroundColor(getResources().getColor(R.color.btn_enable));
                    break;
                case MotionEvent.ACTION_UP:
                    btnLogOut.setBackgroundColor(getResources().getColor(R.color.transparent));
                    break;
            }
            return false;
        });
    }

}