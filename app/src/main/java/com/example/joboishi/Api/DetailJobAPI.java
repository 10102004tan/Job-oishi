package com.example.joboishi.Api;

import com.example.joboishi.Models.Jobs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DetailJobAPI {
    public static String BASE_URL = "http://10.0.2.2:3001/";
    @GET("api/job/id={id}")
    Call<Jobs> getJobDetail(@Path("id") String jobId);
}