package com.example.joboishi.Models;

import android.os.Parcelable;

import com.example.joboishi.Models.data.Benefit;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class JobSearch implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("company_id")
    private String company_id;

    @SerializedName("display_name")
    private String company_name;

    @SerializedName("image_logo")
    private String company_logo;

    @SerializedName("sort_addresses")
    private String sort_addresses;
    @SerializedName("published")
    private String published;
    @SerializedName("benefits")
    private ArrayList<Benefit> benefits;
    private Boolean isChecked = false;

    public JobSearch(int id, String title, String company_id, String company_name, String company_logo, String sort_addresses, String published, ArrayList<Benefit> benefits, Boolean isChecked) {
        this.id = id;
        this.title = title;
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.sort_addresses = sort_addresses;
        this.published = published;
        this.benefits = benefits;
        this.isChecked = isChecked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public JobSearch(int id, String title, String company_id, String company_name, String company_logo, String sort_addresses, String published) {
        this.id = id;
        this.title = title;
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.sort_addresses = sort_addresses;
        this.published = published;
    }

    public JobSearch() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getSort_addresses() {
        return sort_addresses;
    }

    public void setSort_addresses(String sort_addresses) {
        this.sort_addresses = sort_addresses;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobSearch jobSearch = (JobSearch) o;
        return id == jobSearch.id && Objects.equals(title, jobSearch.title) && Objects.equals(company_id, jobSearch.company_id) && Objects.equals(company_name, jobSearch.company_name) && Objects.equals(company_logo, jobSearch.company_logo) && Objects.equals(sort_addresses, jobSearch.sort_addresses) && Objects.equals(published, jobSearch.published) && Objects.equals(isChecked, jobSearch.isChecked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, company_id, company_name, company_logo, sort_addresses, published, isChecked);
    }
}
