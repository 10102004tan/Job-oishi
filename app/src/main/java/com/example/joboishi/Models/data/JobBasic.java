package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

//Class for API
public class JobBasic {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("company_name")
    private String company_name;
    @SerializedName("company_logo")
    private String company_logo;
}
