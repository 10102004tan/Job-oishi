package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.joboishi.Adapters.SimpleStringRecyclerViewAdapter;
import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectGenderFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private ArrayList<String> genderData = new ArrayList<>();
    private String genderSelectedValue = "";
    private TextView genderTextView;

    public ArrayList<String> getGenderData() {
        return genderData;
    }

    public void setGenderData(ArrayList<String> genderData) {
        this.genderData = genderData;
    }

    public String getGenderSelectedValue() {
        return genderSelectedValue;
    }

    public void setGenderSelectedValue(String genderSelectedValue) {
        this.genderSelectedValue = genderSelectedValue;
    }

    public TextView getGenderTextView() {
        return genderTextView;
    }

    public void setGenderTextView(TextView genderTextView) {
        this.genderTextView = genderTextView;
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
        return inflater.inflate(R.layout.bottom_sheet_gender_layout, container, false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }

        RecyclerView listGenderRyc = view.findViewById(R.id.list_gender_ryc);
        SimpleStringRecyclerViewAdapter recyclerViewAdapter = new SimpleStringRecyclerViewAdapter((Activity) getContext(), genderData, listGenderRyc, genderSelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listGenderRyc != null;
        listGenderRyc.setLayoutManager(layoutManager);
        listGenderRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {
                genderSelectedValue = genderData.get(holder.getPos());
                genderTextView.setText(genderSelectedValue);
                onCloseDialog();
            }
        });

        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_gender_dialog);
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
