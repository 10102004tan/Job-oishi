package com.example.joboishi.Models;

public class Company {
    private String name;
    private String logo;
    private String description;
    private String address;

    public Company(String name, String logo, String description, String address) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
