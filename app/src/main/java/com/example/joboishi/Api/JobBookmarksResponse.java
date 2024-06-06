package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class JobBookmarksResponse implements Serializable {

    @SerializedName("data")
    private ArrayList<JobBasic> data;
    @SerializedName("last_page")
    private int totalPages;

    public JobBookmarksResponse(ArrayList<JobBasic> data, int totalPages) {
        this.data = data;
        this.totalPages = totalPages;
    }

    public ArrayList<JobBasic> getData() {
        return data;
    }

    public void setData(ArrayList<JobBasic> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
