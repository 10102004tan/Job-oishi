package com.example.joboishi.ViewModels;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.joboishi.R;

import java.util.Objects;

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);

//        // Remove dialog title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        // Remove default padding of dialog
//        Objects.requireNonNull(getWindow()).getDecorView().setPadding(0, 0, 0, 0);
//
//        // Config LayoutParams
//        WindowManager.LayoutParams params = Objects.requireNonNull(getWindow()).getAttributes();
//        params.gravity = Gravity.CENTER;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        getWindow().setAttributes(params);
//
//        // Translate background
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        // Remove blur effect
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//
//        // Inflate
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_layout, null);
//
//        // Full screen for layout
//        view.setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//        ));
//
//        setContentView(view);
//
//        setTitle(null);
//        setCancelable(false);
//        setOnCancelListener(null);
    }
}
