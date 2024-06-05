package com.example.joboishi.Api;

public class UserForgotPasswordRequest {
    private String email;

    public UserForgotPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

