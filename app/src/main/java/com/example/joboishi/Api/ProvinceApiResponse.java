package com.example.joboishi.Api;

import com.google.gson.annotations.SerializedName;

public class ProvinceApiResponse {

    @SerializedName("idProvince")
    private String provinceCode;

    @SerializedName("name")
    private String provinceName;

    public ProvinceApiResponse(String provinceCode, String provinceName) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
