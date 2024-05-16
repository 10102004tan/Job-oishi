package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

public class Benefit {
    @SerializedName("value")
    private String value;
    @SerializedName("icon")
    private String icon;

    public Benefit(int id, String value, String icon) {
        this.value = value;
        this.icon = icon;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
