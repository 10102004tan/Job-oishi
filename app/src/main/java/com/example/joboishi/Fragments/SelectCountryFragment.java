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
import com.example.joboishi.Adapters.CountryRecyclerViewAdapter;
import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectCountryFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private ArrayList<CountryApiResponse> countryData = new ArrayList<>();
    private String countrySelectedValue = "";
    private TextView countryTextView;
    private String searchingValue = "";
    private ArrayList<CountryApiResponse> temp = new ArrayList<>();

    public void setTemp(ArrayList<CountryApiResponse> temp) {
        this.temp = temp;
    }

    public void setCountryData(ArrayList<CountryApiResponse> countryData) {
        this.countryData = countryData;
    }

    public ArrayList<CountryApiResponse> getCountryData() {
        return countryData;
    }

    public String getCountrySelectedValue() {
        return countrySelectedValue;
    }

    public void setCountrySelectedValue(String countrySelectedValue) {
        this.countrySelectedValue = countrySelectedValue;
    }

    public TextView getCountryTextView() {
        return countryTextView;
    }

    public void setCountryTextView(TextView countryTextView) {
        this.countryTextView = countryTextView;
    }

    public ArrayList<CountryApiResponse> getTemp() {
        return temp;
    }

    public static boolean checkPattern(String inputString, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inputString);
        return m.find();
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
        return inflater.inflate(R.layout.bottom_sheet_country_layout, container, false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }

        RecyclerView listCountryRyc = view.findViewById(R.id.list_country_ryc);
        CountryRecyclerViewAdapter recyclerViewAdapter = new CountryRecyclerViewAdapter((Activity) getContext(), countryData, listCountryRyc, countrySelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listCountryRyc != null;
        listCountryRyc.setLayoutManager(layoutManager);
        listCountryRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new CountryRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CountryRecyclerViewAdapter.MyHolder holder) {
                countrySelectedValue = countryData.get(holder.getPos()).getName();
                countryTextView.setText(countrySelectedValue);
                countryData.clear();
                countryData.addAll(temp);
                searchingValue = "";
                onCloseDialog();
            }
        });

        // Input search country
        EditText searchCountryEdt = view.findViewById(R.id.input_search_country);
        if (!searchingValue.isEmpty()) {
            assert searchCountryEdt != null;
            searchCountryEdt.setText(searchingValue);
        }
        assert searchCountryEdt != null;

        searchCountryEdt.addTextChangedListener(new TextWatcher() {
            final ArrayList<CountryApiResponse> searchData = new ArrayList<>();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingValue = s.toString();

                if (s.toString().isEmpty()) {
                    // If value search empty
                    // Render first list to view
                    countryData.clear();
                    countryData.addAll(temp);
                    recyclerViewAdapter.notifyDataSetChanged();
                } else {
                    // If value search not empty
                    for (CountryApiResponse item :
                            countryData) {
                        if (checkPattern(item.getName().toLowerCase(), ".*" + searchingValue.toLowerCase() + ".*")) {
                            // TolowerCase search value and country value to compare
                            // If country value contains search value => add to new array
                            searchData.add(item);
                        }
                    }
                    // After filter
                    // If have results
                    if (!searchData.isEmpty()) {
                        // Update result to countryData array
                        countryData.clear();
                        countryData.addAll(searchData);
                        searchData.clear(); // Clear data in searchArray after search
                        recyclerViewAdapter.notifyDataSetChanged(); // Update to recycler view
                    }
                }
            }
        });

        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_country_dialog);
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
