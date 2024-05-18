package com.example.joboishi.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobBasic implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("company_id")
    private String company_id;

    @SerializedName("company_name")
    private String company_name;

    @SerializedName("company_logo")
    private String company_logo;

    @SerializedName("sort_addresses")
    private String sort_addresses;

    @SerializedName("is_salary_visible")
    private boolean is_salary_visible = false;

    @SerializedName("published")
    private  String published;

    public JobBasic(int id, String title, String company_id, String company_name, String company_logo, String sort_addresses, boolean is_salary_visible, String published) {
        this.id = id;
        this.title = title;
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.sort_addresses = sort_addresses;
        this.is_salary_visible = is_salary_visible;
        this.published = published;
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

    public boolean isIs_salary_visible() {
        return is_salary_visible;
    }

    public void setIs_salary_visible(boolean is_salary_visible) {
        this.is_salary_visible = is_salary_visible;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public JobBasic() {
    }
}
