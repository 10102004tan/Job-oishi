package com.example.joboishi.Models;

public class Job {
    private String title;
    private String content;
    private String website;
    private Company company;

    public Job(String title, String content, String website, Company company) {
        this.title = title;
        this.content = content;
        this.website = website;
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
