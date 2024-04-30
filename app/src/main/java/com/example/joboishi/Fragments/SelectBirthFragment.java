package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.CityRecyclerViewAdapter;
import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.R;
import com.example.joboishi.Views.TimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectBirthFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private int dayOfBirth;
    private int monthOfBirth;
    private int yearOfBirth;
    private String birthDate = ""; // birth date of user

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public TextView getBirthDayTextView() {
        return birthDayTextView;
    }

    public void setBirthDayTextView(TextView birthDayTextView) {
        this.birthDayTextView = birthDayTextView;
    }

    private TextView birthDayTextView;

    public Fragment getFragment() {
        return fragment;
    }
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
    public static MyBottomSheetDialogFragment newInstance() {
        return new MyBottomSheetDialogFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_birth_layout, container, false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }

        // Get date when user choose birth date
        TimePicker dayPicker = view.findViewById(R.id.day_picker);
        int day = Integer.parseInt(birthDate.split("/")[0]);
        assert dayPicker != null;
        dayPicker.setValue(day);

        // Get month of date when user choose birth date
        TimePicker monthPicker = view.findViewById(R.id.month_picker);
        int month = Integer.parseInt(birthDate.split("/")[1]);
        assert monthPicker != null;
        monthPicker.setValue(month);

        // Get year of date when user choose birth date
        TimePicker yearPicker = view.findViewById(R.id.year_picker);
        int year = Integer.parseInt(birthDate.split("/")[2]);
        assert yearPicker != null;
        yearPicker.setValue(year);


        // Save chosen birth date
        Button buttonSaveDate = view.findViewById(R.id.btn_save_birth);
        assert buttonSaveDate != null;
        buttonSaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfBirth = dayPicker.getValue();
                monthOfBirth = monthPicker.getValue();
                yearOfBirth = yearPicker.getValue();

                birthDate = dayOfBirth < 10 ? "0" + dayOfBirth + "/" : dayOfBirth + "/";
                birthDate += monthOfBirth < 10 ? "0" + monthOfBirth + "/" : monthOfBirth + "/";
                birthDate += yearOfBirth;

                birthDayTextView.setText(birthDate);
                onCloseDialog();
            }
        });

        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_birth_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseDialog();
            }
        });
    }
    public void  onCloseDialog() {
        MyBottomSheetDialogFragment bottomSheetFragment = (MyBottomSheetDialogFragment) getParentFragment();
        if (bottomSheetFragment != null) {
            bottomSheetFragment.dismiss();
        }
    }
}
