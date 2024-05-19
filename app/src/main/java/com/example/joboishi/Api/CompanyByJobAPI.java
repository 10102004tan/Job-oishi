package com.example.joboishi.Api;

import com.example.joboishi.Models.data.Company;
import com.example.joboishi.Models.data.Job;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CompanyByJobAPI {
    public static String BASE_URL = "http://10.0.2.2:3001";
    @GET("api/company/{id}")
    Call<Company> getCompanyByJob(@Path("id") String company_id);
}
