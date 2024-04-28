package com.example.joboishi.Activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.joboishi.Fragments.BottomSheetDialog.BtsDeleteAccountFragment;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.R;
import com.example.joboishi.databinding.AccountLayoutBinding;

public class AccountSettingActivity extends AppCompatActivity {

    private AccountLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AccountLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatButton btnChangeEmail = binding.btnChangeEmail;
        AppCompatButton btnChangePass = binding.buttonchangpw;
        AppCompatButton btnChangePhone = binding.btnPhoneNumber;
        AppCompatButton btnDeleteAccount = binding.btnDeleteAccount;

        changBackGroundButton(btnChangeEmail, btnChangePass, btnChangePhone, btnDeleteAccount);

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();
                dialogFragment.setFragment(new BtsDeleteAccountFragment());
                dialogFragment.show(getSupportFragmentManager(), "del account");
            }
        });
    }

    public void changBackGroundButton(AppCompatButton... buttons) {
        for (AppCompatButton button : buttons) {
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.button_background_change);
            animatorSet.setTarget(button);

            button.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setBackgroundColor(getResources().getColor(R.color.btn_enable));
                        break;
                    case MotionEvent.ACTION_UP:
                        button.setBackgroundColor(getResources().getColor(R.color.transparent));
                        break;
                }
                return false;
            });
        }
    }

}