package com.example.joboishi.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.SimpleStringRecyclerViewAdapter;
import com.example.joboishi.Fragments.BottomSheetDialog.OptionExperienceFragment;
import com.example.joboishi.Fragments.BottomSheetDialog.OptionTypeJobFragment;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.ViewModels.ScrollRecyclerviewListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vdx.designertoast.DesignerToast;

import java.util.ArrayList;
public class SelectFilterJob extends BottomSheetDialogFragment {
    private ArrayList<String> list;
    private String selectedValue = "";
    private String titleFilter;

    public String getTitleFilter() {
        return titleFilter;
    }

    public void setTitleFilter(String titleFilter) {
        this.titleFilter = titleFilter;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public static MyBottomSheetDialogFragment newInstance() {
        return new MyBottomSheetDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_find_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(fragment != null) {
            fragment = getFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, fragment).commit();
        }

        HomeViewModel homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        TextView lblTitleFilter = view.findViewById(R.id.lbl_title_filter);
        lblTitleFilter.setText(titleFilter);

        RecyclerView listType = view.findViewById(R.id.list_type);
        SimpleStringRecyclerViewAdapter  recyclerViewAdapter = new SimpleStringRecyclerViewAdapter((Activity) getContext(),list, listType, selectedValue );

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listType.setLayoutManager(layoutManager);
        listType.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {

                selectedValue = list.get(holder.getPos());
                homeViewModel.setSelectedValue(selectedValue);
                onCloseDialog();
            }
        });

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

