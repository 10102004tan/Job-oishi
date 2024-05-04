package com.example.joboishi.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Jobs {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("requirements")
    private String requirements;
    @SerializedName("responsibilities")
    private String responsibilities;
    @SerializedName("company_id")
    private String company_id;
    @SerializedName("company_name")
    private String company_name;
    @SerializedName("company_logo")
    private String company_logo;
    @SerializedName("skills")
    private ArrayList<String> skills;
    @SerializedName("experience")
    private String experience;
    @SerializedName("is_edit")
    private int is_edit;
    @SerializedName("is_salary_value")
    private String is_salary_value;
    @SerializedName("job_level")
    private String job_level;
    @SerializedName("is_applied")
    private boolean is_applied;

    public Jobs(){}

    public Jobs(String id, String title, String content, String requirements, String responsibilities, String company_id, String company_name, String company_logo, ArrayList<String> skills, String experience, int is_edit, String is_salary_value, String job_level, boolean is_applied) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.requirements = requirements;
        this.responsibilities = responsibilities;
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.skills = skills;
        this.experience = experience;
        this.is_edit = is_edit;
        this.is_salary_value = is_salary_value;
        this.job_level = job_level;
        this.is_applied = is_applied;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
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

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(int is_edit) {
        this.is_edit = is_edit;
    }

    public String getIs_salary_value() {
        return is_salary_value;
    }

    public void setIs_salary_value(String is_salary_value) {
        this.is_salary_value = is_salary_value;
    }

    public String getJob_level() {
        return job_level;
    }

    public void setJob_level(String job_level) {
        this.job_level = job_level;
    }

    public boolean isIs_applied() {
        return is_applied;
    }

    public void setIs_applied(boolean is_applied) {
        this.is_applied = is_applied;
    }
}
