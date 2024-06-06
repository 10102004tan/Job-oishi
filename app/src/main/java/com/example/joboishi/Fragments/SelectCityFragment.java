package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.CityRecyclerViewAdapter;
import com.example.joboishi.Adapters.CountryRecyclerViewAdapter;
import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectCityFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private ArrayList<ProvinceApiResponse> cityData = new ArrayList<>();
    private String citySelectedValue = "";
    private TextView cityTextView;
    private String searchingValue = "";
    private ArrayList<ProvinceApiResponse> temp = new ArrayList<>();
    public void setTemp(ArrayList<ProvinceApiResponse> temp) {
        this.temp = temp;
    }
    public ArrayList<ProvinceApiResponse> getTemp() {
        return temp;
    }

    public ArrayList<ProvinceApiResponse> getCityData() {
        return cityData;
    }

    public void setCityData(ArrayList<ProvinceApiResponse> cityData) {
        this.cityData = cityData;
    }

    public String getCitySelectedValue() {
        return citySelectedValue;
    }

    public void setCitySelectedValue(String citySelectedValue) {
        this.citySelectedValue = citySelectedValue;
    }

    public TextView getCityTextView() {
        return cityTextView;
    }

    public void setCityTextView(TextView cityTextView) {
        this.cityTextView = cityTextView;
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
        return inflater.inflate(R.layout.bottom_sheet_city_layout, container, false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }
        RecyclerView listCityRyc = view.findViewById(R.id.list_city_ryc);
        CityRecyclerViewAdapter recyclerViewAdapter = new CityRecyclerViewAdapter((Activity) getContext(), cityData, listCityRyc, citySelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listCityRyc != null;
        listCityRyc.setLayoutManager(layoutManager);
        listCityRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new CityRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CityRecyclerViewAdapter.MyHolder holder) {
                citySelectedValue = cityData.get(holder.getPos()).getProvinceName();
                cityTextView.setText(citySelectedValue);
                cityData.clear();
                cityData.addAll(temp);
                searchingValue = "";
                onCloseDialog();
            }
        });

        // Input search city
        EditText searchCityEdt = view.findViewById(R.id.input_search_city);
        if (!searchingValue.isEmpty()) {
            assert searchCityEdt != null;
            searchCityEdt.setText(searchingValue);
        }
        assert searchCityEdt != null;

        searchCityEdt.addTextChangedListener(new TextWatcher() {
            final ArrayList<ProvinceApiResponse> searchData = new ArrayList<>();

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
                    cityData.clear();
                    cityData.addAll(temp);
                    recyclerViewAdapter.notifyDataSetChanged();
                } else {
                    // If value search not empty
                    for (ProvinceApiResponse item :
                            cityData) {
                        if (checkPattern(item.getProvinceName().toLowerCase(), ".*" + searchingValue.toLowerCase() + ".*")) {
                            // TolowerCase search value and country value to compare
                            // If country value contains search value => add to new array
                            searchData.add(item);
                        }
                    }
                    // After filter
                    // If have results
                    if (!searchData.isEmpty()) {
                        // Update result to countryData array
                        cityData.clear();
                        cityData.addAll(searchData);
                        searchData.clear(); // Clear data in searchArray after search
                        recyclerViewAdapter.notifyDataSetChanged(); // Update to recycler view
                    }
                }
            }
        });

        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_city_dialog);
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
