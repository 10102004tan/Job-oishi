package com.example.joboishi.Api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("role")
    private int role;
    @SerializedName("email_verified_at")
    private String emailVerifiedAt;
    @SerializedName("birth")
    private String birth;
    @SerializedName("gender")
    private String gender;
    @SerializedName("education")
    private String education;
    @SerializedName("time_starting_work")
    private String timeStartingWorking;
    @SerializedName("photo_url")
    private String photoUrl;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("province")
    private String province;
    @SerializedName("login_method")
    private String loginMethod;

    public UserResponse(int id, String firstName, String lastName, String email, String phone, int role, String emailVerifiedAt, String birth, String gender, String education, String timeStartingWorking, String photoUrl, String country, String city, String province, String loginMethod) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.emailVerifiedAt = emailVerifiedAt;
        this.birth = birth;
        this.gender = gender;
        this.education = education;
        this.timeStartingWorking = timeStartingWorking;
        this.photoUrl = photoUrl;
        this.country = country;
        this.city = city;
        this.province = province;
        this.loginMethod = loginMethod;
    }

    public UserResponse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
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

    public String getTimeStartingWorking() {
        return timeStartingWorking;
    }

    public void setTimeStartingWorking(String timeStartingWorking) {
        this.timeStartingWorking = timeStartingWorking;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }
}
