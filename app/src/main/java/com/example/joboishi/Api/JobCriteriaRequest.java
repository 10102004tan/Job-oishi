package com.example.joboishi.Api;

public class JobCriteriaRequest {
    private int user_id;
    private String job_position;
    private String job_location;
    private String job_salary;
    private String working_form;
    private int is_remote;

    public JobCriteriaRequest() {
    }

    public JobCriteriaRequest(int user_id, String job_position, String job_location, String job_salary, String working_form, int is_remote) {
        this.user_id = user_id;
        this.job_position = job_position;
        this.job_location = job_location;
        this.job_salary = job_salary;
        this.working_form = working_form;
        this.is_remote = is_remote;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getJob_position() {
        return job_position;
    }

    public void setJob_position(String job_position) {
        this.job_position = job_position;
    }

    public String getJob_location(StringBuilder cities) {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getJob_salary() {
        return job_salary;
    }

    public void setJob_salary(String job_salary) {
        this.job_salary = job_salary;
    }

    public String getWorking_form() {
        return working_form;
    }

    public void setWorking_form(String working_form) {
        this.working_form = working_form;
    }

    public int getIs_remote() {
        return is_remote;
    }

    public void setIs_remote(int is_remote) {
        this.is_remote = is_remote;
    }
}
