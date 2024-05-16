package com.example.joboishi.Models.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Job {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("responsibilities")
    private String responsibilities;
    @SerializedName("requirements")
    private String requirements;
    @SerializedName("company")
    private Company company;
    @SerializedName("skills")
    private ArrayList<String> skills;
    @SerializedName("experience")
    private String experience;
    @SerializedName("job_type_str")
    private String job_type_str;
    @SerializedName("job_level")
    private String job_level;
    @SerializedName("recruitment_process")
    private ArrayList<String> recruitment_process;
    @SerializedName("salary_value")
    private String salary_value;
    @SerializedName("is_salary_visible")
    private boolean is_salary_visible;
    @SerializedName("benefits")
    private ArrayList<Benefit> benefit;
    @SerializedName("is_edit")
    private boolean is_edit;
    @SerializedName("is_applied")
    private boolean is_applied;
//    @SerializedName("created_at")
//    private Date created_at;
//    @SerializedName("update_at")
//    private Date update_at;


    public Job(String id, String title, String content, String responsibilities, String requirements, Company company, ArrayList<String> skills, String experience, String job_type_str, String job_level, ArrayList<String> recruitment_process, String salary_value, boolean is_salary_visible, ArrayList<Benefit> benefit, boolean is_edit, boolean is_applied) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.responsibilities = responsibilities;
        this.requirements = requirements;
        this.company = company;
        this.skills = skills;
        this.experience = experience;
        this.job_type_str = job_type_str;
        this.job_level = job_level;
        this.recruitment_process = recruitment_process;
        this.salary_value = salary_value;
        this.is_salary_visible = is_salary_visible;
        this.benefit = benefit;
        this.is_edit = is_edit;
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

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public String getJob_type_str() {
        return job_type_str;
    }

    public void setJob_type_str(String job_type_str) {
        this.job_type_str = job_type_str;
    }

    public String getJob_level() {
        return job_level;
    }

    public void setJob_level(String job_level) {
        this.job_level = job_level;
    }

    public ArrayList<String> getRecruitment_process() {
        return recruitment_process;
    }

    public void setRecruitment_process(ArrayList<String> recruitment_process) {
        this.recruitment_process = recruitment_process;
    }

    public String getSalary_value() {
        return salary_value;
    }

    public void setSalary_value(String salary_value) {
        this.salary_value = salary_value;
    }

    public boolean getIs_salary_visible() {
        return is_salary_visible;
    }

    public void setIs_salary_visible(boolean is_salary_visible) {
        this.is_salary_visible = is_salary_visible;
    }

    public ArrayList<Benefit> getBenefit() {
        return benefit;
    }

    public void setBenefit(ArrayList<Benefit> benefit) {
        this.benefit = benefit;
    }

    public boolean isIs_edit() {
        return is_edit;
    }

    public void setIs_edit(boolean is_edit) {
        this.is_edit = is_edit;
    }

    public boolean isIs_applied() {
        return is_applied;
    }

    public void setIs_applied(boolean is_applied) {
        this.is_applied = is_applied;
    }
}