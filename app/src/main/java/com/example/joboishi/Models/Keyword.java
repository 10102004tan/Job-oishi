package com.example.joboishi.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Keyword implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("keyword")
    private String keyword;
    private Boolean isChecked = false;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Keyword(int id, String name, String keyword) {
        this.id = id;
        this.name = name;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Keyword() {
    }
}
