package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.joboishi.Adapters.CountryRecyclerViewAdapter;
import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.CountryApi;
import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCountryFragment extends BottomSheetDialogFragment {
    private Fragment fragment;

    private final ArrayList<CountryApiResponse> countryData = new ArrayList<>();

    private String countrySelectedValue = "";

    private CountryRecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<CountryApiResponse> temp = new ArrayList<>();

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

        countryData.addAll(temp);


        // Handle with view here
        RecyclerView listCountryRyc = view.findViewById(R.id.list_country_ryc);
        recyclerViewAdapter = new CountryRecyclerViewAdapter((Activity) getContext(), countryData, listCountryRyc, countrySelectedValue);

        if (countryData.isEmpty()) {
            // Get country data from api
            CountryApi countryApi = ApiClient.getCountryAPI();
            Call<ArrayList<CountryApiResponse>> callCountry = countryApi.getData();
            callCountry.enqueue(new Callback<ArrayList<CountryApiResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<CountryApiResponse>> call, @NonNull Response<ArrayList<CountryApiResponse>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Log.d("COUNTRY_SIZE", response.body().size() + "");
                        countryData.addAll(response.body());
                        temp = new ArrayList<>(countryData);
                        recyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("COUNTRY_API_ERROR", "ERROR");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<CountryApiResponse>> call, @NonNull Throwable t) {
                    Log.d("COUNTRY_API_ERROR", t.toString());
                }
            });
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert listCountryRyc != null;
        listCountryRyc.setLayoutManager(layoutManager);
        listCountryRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
