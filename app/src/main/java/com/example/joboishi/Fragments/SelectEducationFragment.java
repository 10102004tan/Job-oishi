package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.SimpleStringRecyclerViewAdapter;
import com.example.joboishi.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class SelectEducationFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private ArrayList<String> eduData = new ArrayList<>();
    private String eduSelectedValue = "";
    private TextView eduTextView;


    public ArrayList<String> getEduData() {
        return eduData;
    }

    public void setEduData(ArrayList<String> eduData) {
        this.eduData = eduData;
    }

    public String getEduSelectedValue() {
        return eduSelectedValue;
    }

    public void setEduSelectedValue(String eduSelectedValue) {
        this.eduSelectedValue = eduSelectedValue;
    }

    public TextView getEduTextView() {
        return eduTextView;
    }

    public void setEduTextView(TextView eduTextView) {
        this.eduTextView = eduTextView;
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
        return inflater.inflate(R.layout.bottom_sheet_edu_layout, container, false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }


        RecyclerView listEduRyc = view.findViewById(R.id.list_edu_ryc);
        SimpleStringRecyclerViewAdapter recyclerViewAdapter = new SimpleStringRecyclerViewAdapter((Activity) getContext(), eduData, listEduRyc, eduSelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listEduRyc != null;
        listEduRyc.setLayoutManager(layoutManager);
        listEduRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {
                eduSelectedValue = eduData.get(holder.getPos());
                eduTextView.setText(eduSelectedValue);
                onCloseDialog();
            }
        });

        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_edu_dialog);
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
