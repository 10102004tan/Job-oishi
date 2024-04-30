package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.joboishi.R;
import com.example.joboishi.Views.TimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SelectSalaryFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private int minSalarySelected;

    private int maxSalarySelected;

    private TextView salaryTextView;

    public int getMinSalarySelected() {
        return minSalarySelected;
    }

    public void setMinSalarySelected(int minSalarySelected) {
        this.minSalarySelected = minSalarySelected;
    }

    public int getMaxSalarySelected() {
        return maxSalarySelected;
    }

    public void setMaxSalarySelected(int maxSalarySelected) {
        this.maxSalarySelected = maxSalarySelected;
    }

    public TextView getSalaryTextView() {
        return salaryTextView;
    }

    public void setSalaryTextView(TextView salaryTextView) {
        this.salaryTextView = salaryTextView;
    }


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
        return inflater.inflate(R.layout.bottom_sheet_salary_layout, container, false);
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }

        // Set salary selected of user to view
        salaryTextView.setText(minSalarySelected + " - " + maxSalarySelected + " Tr");

        // Set min salary selected to picker
        TimePicker minPicker = view.findViewById(R.id.min_salary_picker);
        minPicker.setValue(minSalarySelected);

        // Set max salary selected to picker
        TimePicker maxPicker = view.findViewById(R.id.max_salary_picker);
        maxPicker.setValue(maxSalarySelected);


        // Save chosen birth date
        Button buttonSaveSalary = view.findViewById(R.id.btn_save_salary_bottom_sheet);
        assert buttonSaveSalary != null;
        buttonSaveSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minSalarySelected = minPicker.getValue();
                maxSalarySelected = maxPicker.getValue();
                if (minSalarySelected > maxSalarySelected) {
                    maxSalarySelected = minSalarySelected +  1;
                }

                salaryTextView.setText(minSalarySelected + " - " + maxSalarySelected + " Tr");
                onCloseDialog();
            }
        });


        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_salary_dialog);
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
