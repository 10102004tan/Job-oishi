package com.example.joboishi.ViewModels;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.Models.JobBasic;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> selectedValue = new MutableLiveData<String>("");
    private MutableLiveData<Integer> currentTabPosition = new MutableLiveData<>(0);

    public MutableLiveData<String> getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue.setValue(selectedValue);
    }

    public LiveData<Integer> getCurrentTabPosition() {
        return currentTabPosition;
    }

    public void setCurrentTabPosition(int position) {
        currentTabPosition.setValue(position);
    }


}
