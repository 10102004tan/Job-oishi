package com.example.joboishi.Api;

import com.example.joboishi.Models.Job;
import com.example.joboishi.Models.Jobs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DetailJobAPI {
    public static String BASE_URL = "http://10.0.2.2:3001/";
    @GET("api/job/")
    Call <Jobs> getJobDetail(@Query("id") String jobId);
}
