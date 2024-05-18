package com.example.joboishi.Api;

import com.example.joboishi.Models.Jobs;
import com.example.joboishi.Models.data.Job;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DetailJobAPI {
    public static String BASE_URL = "http://ip172-18-0-7-cp48j2ol2o9000avmmug-3001.direct.labs.play-with-docker.com/";

    @GET("api/job/id={id}")
    Call<Job> getJobDetail(@Path("id") String jobId);
}
