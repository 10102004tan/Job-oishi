package com.example.joboishi.Api;

public class UserRequest {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String birth;
    private String gender;
    private String education;
    private String time_starting_work;
    private String photo_url;
    private String country;
    private String city;
    private String province;
    private int is_first_login;
    private String verify_token_code;


    public UserRequest(int id, String first_name, String last_name, String email, String birth, String gender, String education, String time_starting_work, String photo_url, String country, String city, String province, int is_first_login) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
        this.education = education;
        this.time_starting_work = time_starting_work;
        this.photo_url = photo_url;
        this.country = country;
        this.city = city;
        this.province = province;
        this.is_first_login = is_first_login;
    }


    public String getVerify_token_code() {
        return verify_token_code;
    }

    public void setVerify_token_code(String verify_token_code) {
        this.verify_token_code = verify_token_code;
    }

    public int getIs_first_login() {
        return is_first_login;
    }

    public void setIs_first_login(int is_first_login) {
        this.is_first_login = is_first_login;
    }

    public UserRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getTime_starting_work() {
        return time_starting_work;
    }

    public void setTime_starting_work(String time_starting_work) {
        this.time_starting_work = time_starting_work;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
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
}
