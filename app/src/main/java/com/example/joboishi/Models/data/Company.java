package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Company {
    @SerializedName("id")
    private int id;
    @SerializedName("display_name")
    private String display_name;
    @SerializedName("image_logo")
    private String image_logo;
    @SerializedName("description")
    private String description;
    @SerializedName("website")
    private String website;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("company_size")
    private String company_size;
    @SerializedName("address")
    private ArrayList<Address> address;
    @SerializedName("num_job_openings")
    private int num_job_openings;
    @SerializedName("benefits")
    private ArrayList<Benefit> benefits;
    @SerializedName("nationalities")
    private ArrayList<Nationality> nationalities;

    public Company(int id, String display_name, String image_logo, String description, String website, String tagline, String company_size, ArrayList<Address> address, int num_job_openings, ArrayList<Benefit> benefits, ArrayList<Nationality> nationalities) {
        this.id = id;
        this.display_name = display_name;
        this.image_logo = image_logo;
        this.description = description;
        this.website = website;
        this.tagline = tagline;
        this.company_size = company_size;
        this.address = address;
        this.num_job_openings = num_job_openings;
        this.benefits = benefits;
        this.nationalities = nationalities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getImage_logo() {
        return image_logo;
    }

    public void setImage_logo(String image_logo) {
        this.image_logo = image_logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getCompany_size() {
        return company_size;
    }

    public void setCompany_size(String company_size) {
        this.company_size = company_size;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public int getNum_job_openings() {
        return num_job_openings;
    }

    public void setNum_job_openings(int num_job_openings) {
        this.num_job_openings = num_job_openings;
    }

    public ArrayList<Benefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(ArrayList<Benefit> benefits) {
        this.benefits = benefits;
    }

    public ArrayList<Nationality> getNationalities() {
        return nationalities;
    }

    public void setNationalities(ArrayList<Nationality> nationalities) {
        this.nationalities = nationalities;
    }
}
