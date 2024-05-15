package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

public class Nationality {
    @SerializedName("id")
    private int id;
    @SerializedName("national")
    private String national;
    @SerializedName("flag")
    private String flag;
    @SerializedName("company_id")
    private int company_id;
}
