package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("id")
    private int id;
    @SerializedName("id_company")
    private int id_company;
    @SerializedName("street")
    private String street;
    @SerializedName("district")
    private String district;
    @SerializedName("province")
    private String province;

    public Address(int id, int id_company, String street, String district, String province) {
        this.id = id;
        this.id_company = id_company;
        this.street = street;
        this.district = district;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_company() {
        return id_company;
    }

    public void setId_company(int id_company) {
        this.id_company = id_company;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
