package com.example.joboishi.Api;

import com.google.gson.annotations.SerializedName;

public class UserApiResponse {

    /*
     *
     * "id": 1,
     * "first_name": "Test User",
     * "email": "test@example.com",
     * "email_verified_at": "2024-04-29T12:45:55.000000Z",
     * "created_at": "2024-04-29T12:45:55.000000Z",
     * "updated_at": "2024-04-29T12:45:55.000000Z",
     * "last_name": "",
     * "birth": null,
     * "gender": "",
     * "education": null,
     * "time_starting_work": null,
     * "photo_url": null
     *
     * */

    @SerializedName("id")
    private int id;

    @SerializedName("first_name")
    private String firstname;
    @SerializedName("last_name")
    private String lastname;

    @SerializedName("email")

    private String email;

    @SerializedName("birth")
    private String birth;

    @SerializedName("gender")
    private String gender;

    @SerializedName("education")
    private String education;

    @SerializedName("photo_url")
    private String photoUrl;

    @SerializedName("time_starting_work")
    private String timeStartingWork;

    public UserApiResponse(int id, String firstname, String lastname, String email, String birth, String gender, String education, String photoUrl, String timeStartingWork) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
        this.education = education;
        this.photoUrl = photoUrl;
        this.timeStartingWork = timeStartingWork;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTimeStartingWork() {
        return timeStartingWork;
    }

    public void setTimeStartingWork(String timeStartingWork) {
        this.timeStartingWork = timeStartingWork;
    }
}
