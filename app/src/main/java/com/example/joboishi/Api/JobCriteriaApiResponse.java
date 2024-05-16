package com.example.joboishi.Api;

import com.google.gson.annotations.SerializedName;

public class JobCriteriaApiResponse {

    /*
    {
            "id": 1,
            "user_id": 1,
            "job_position": "Frontend Engineer",
            "job_location": "Ho Chi Minh",
            "job_salary": "6,7",
            "working_form": "fulltime,remote",
            "is_remote": 1,
            "created_at": "2024-05-13T14:56:37.000000Z",
            "updated_at": "2024-05-16T06:21:34.000000Z"
    }
     */

    @SerializedName("job_salary")
    private String jobSalary;
    @SerializedName("working_form")
    private String workingForm;
    @SerializedName("job_position")
    private String jobPosition;
    @SerializedName("is_remote")
    private String isRemote;
    @SerializedName("job_location")
    private String lastname;
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private String firstname;

    public JobCriteriaApiResponse() {
    }

    public JobCriteriaApiResponse(String jobSalary, String workingForm, String jobPosition, String isRemote, String lastname, int id, String firstname) {
        this.jobSalary = jobSalary;
        this.workingForm = workingForm;
        this.jobPosition = jobPosition;
        this.isRemote = isRemote;
        this.lastname = lastname;
        this.id = id;
        this.firstname = firstname;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSlary) {
        this.jobSalary = jobSlary;
    }

    public String getWorkingForm() {
        return workingForm;
    }

    public void setWorkingForm(String workingForm) {
        this.workingForm = workingForm;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(String isRemote) {
        this.isRemote = isRemote;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
