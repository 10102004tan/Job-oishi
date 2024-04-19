package com.datto.demo_android.models;

public class CountryRyc {
    private String countryName;
    private boolean isSelect;

    public CountryRyc(String countryName, boolean isSelect) {
        this.countryName = countryName;
        this.isSelect = isSelect;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
