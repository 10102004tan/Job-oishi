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
    private int isRemote;
    @SerializedName("job_location")
    private String jobLocation;
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;

    public JobCriteriaApiResponse() {
    }

    public JobCriteriaApiResponse(String jobSalary, String workingForm, String jobPosition, int isRemote, String jobLocation, int id, int userId) {
        this.jobSalary = jobSalary;
        this.workingForm = workingForm;
        this.jobPosition = jobPosition;
        this.isRemote = isRemote;
        this.jobLocation = jobLocation;
        this.id = id;
        this.userId = userId;
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

    public int getIsRemote() {
        return isRemote;
    }

    public void setIsRemote(int isRemote) {
        this.isRemote = isRemote;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
