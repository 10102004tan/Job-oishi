package com.example.joboishi.Fragments.BottomSheetDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.OptionAdapter;
import com.example.joboishi.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Arrays;

public class OptionTypeJobFragment extends Fragment {
    private OptionAdapter adapter;
    private RecyclerView recyclerView;
    private TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout first
        View view = inflater.inflate(R.layout.bottom_sheet_option_type_job_layout, container, false);

        // Then find the RecyclerView within the inflated layout
        title = view.findViewById(R.id.title);
        recyclerView = view.findViewById(R.id.list_option_type_job);

        title.setText("Loại công việc");
        // Create list of options
        ArrayList<String> listOption = new ArrayList<>(Arrays.asList("Toàn thời gian", "Bán thời gian", "Thực tập", "Mỗi ngày"));

        // Initialize adapter with data
        adapter = new OptionAdapter(getActivity(), listOption);

        // Set up FlexboxLayoutManager
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        // Set the layout manager and adapter for RecyclerView
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
