package com.example.joboishi.Fragments.BottomSheetDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.joboishi.Activities.SplashActivity;
import com.example.joboishi.R;
import com.example.joboishi.databinding.BottomSheetLogoutLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BtsLogoutFragment extends BottomSheetDialogFragment {
    private BottomSheetLogoutLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetLogoutLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa thông tin người dùng trong SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Xóa tất cả dữ liệu
                editor.clear();
                editor.apply();

                // Chuyển đến SplashActivity
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Kết thúc Activity hiện tại
                getActivity().finish();
                // Đóng BottomSheet
                dismiss();
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
