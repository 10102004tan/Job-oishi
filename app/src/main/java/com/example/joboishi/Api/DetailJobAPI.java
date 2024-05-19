package com.example.joboishi.Api;

import com.example.joboishi.Models.Jobs;
import com.example.joboishi.Models.data.Job;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DetailJobAPI {

    public static String BASE_URL = APIURL.BASE_URL;
    @GET("api/job/{id}")

    Call<Job> getJobDetail(@Path("id") String jobId);
}
