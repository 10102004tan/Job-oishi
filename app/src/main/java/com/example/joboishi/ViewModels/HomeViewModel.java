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
    private MutableLiveData<Integer> currentTotalBookmark = new MutableLiveData<>(0);
    private MutableLiveData<Integer> currentTabPosition = new MutableLiveData<>(0);
    private MutableLiveData<String> selectedValueTopDev = new MutableLiveData<>("Tất cả");
    private MutableLiveData<String> selectedValueJoboishi = new MutableLiveData<>("Tất cả");
    private MutableLiveData<ArrayList<JobBasic>> jobsBookmark = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<JobBasic>> getJobsBookmark() {
        return jobsBookmark;
    }
    public void setJobsBookmark(JobBasic ... jobsBookmark) {
        ArrayList<JobBasic> jobBasics = this.jobsBookmark.getValue();
        for (JobBasic jobBasic : jobsBookmark) {
            jobBasics.add(jobBasic);
        }
        this.jobsBookmark.setValue(jobBasics);
    }

    public MutableLiveData<String> getSelectedValueJoboishi() {
        return selectedValueJoboishi;
    }

    public void setSelectedValueJoboishi(String selectedValueJoboishi) {
        this.selectedValueJoboishi.setValue(selectedValueJoboishi);
    }

    public MutableLiveData<String> getSelectedValueTopDev() {
        return selectedValueTopDev;
    }

    public void setSelectedValueTopDev(String selectedValueTopDev) {
        this.selectedValueTopDev.setValue(selectedValueTopDev);
    }

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

    public MutableLiveData<Integer> getCurrentTotalBookmark() {
        return currentTotalBookmark;
    }

    public void setCurrentTotalBookmark(int currentTotalBookmark){
        this.currentTotalBookmark.setValue(currentTotalBookmark);
    }
}
