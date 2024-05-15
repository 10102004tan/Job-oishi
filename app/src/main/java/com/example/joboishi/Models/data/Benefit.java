package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

public class Benefit {
    @SerializedName("id")
    private int id;
    @SerializedName("value")
    private String value;
    @SerializedName("icon")
    private String icon;

    public Benefit(int id, String value, String icon) {
        this.id = id;
        this.value = value;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
