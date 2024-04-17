package com.example.joboishi.models;

import java.util.Objects;

public class CityMajors {
    private String name_city;
    private Boolean checked_city;

    public String getName_city() {
        return name_city;
    }

    public void setName_city(String name_city) {
        this.name_city = name_city;
    }

    public Boolean getChecked_city() {
        return checked_city;
    }

    public void setChecked_city(Boolean checked_city) {
        this.checked_city = checked_city;
    }

    public CityMajors(String name_city, Boolean checked_city) {
        this.name_city = name_city;
        this.checked_city = checked_city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityMajors that = (CityMajors) o;
        return Objects.equals(name_city, that.name_city) && Objects.equals(checked_city, that.checked_city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name_city, checked_city);
    }

    @Override
    public String toString() {
        return "CityMajors{" +
                "name_city='" + name_city + '\'' +
                ", checked_city=" + checked_city +
                '}';
    }
}
