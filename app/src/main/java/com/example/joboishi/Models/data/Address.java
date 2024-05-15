package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("street")
    private String street;
    @SerializedName("district")
    private String district;
    @SerializedName("province")
    private String province;

    public Address(String street, String district, String province) {
        this.street = street;
        this.district = district;
        this.province = province;
    }

    @Override
    public String toString() {
        return street + ", " + district + ", " + province;
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
