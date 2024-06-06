package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class JobsResponse implements Serializable {
    @SerializedName("last_page")
    private int lastPage;
    @SerializedName("data")
    private ArrayList<JobBasic> listJobs;
    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public ArrayList<JobBasic> getListJobs() {
        return listJobs;
    }

    public void setListJobs(ArrayList<JobBasic> listJobs) {
        this.listJobs = listJobs;
    }

    public JobsResponse(int lastPage, ArrayList<JobBasic> listJobs) {
        this.lastPage = lastPage;
        this.listJobs = listJobs;
    }
}
