package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

public class Nationality {
    @SerializedName("national")
    private String national;
    @SerializedName("flag")
    private String flag;

    public Nationality(String national, String flag) {
        this.national = national;
        this.flag = flag;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
