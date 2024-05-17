package com.example.joboishi.Api;

import com.example.joboishi.Models.Jobs;
import com.example.joboishi.Models.data.Job;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DetailJobAPI {
    public static String BASE_URL = "http://10.0.2.2:3001/";
    @GET("api/job/id={id}")
    Call<Job> getJobDetail(@Path("id") String jobId);
}
