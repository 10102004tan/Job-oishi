package com.example.joboishi.Api;

import com.example.joboishi.Models.Job;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
public interface IJobsService {
    public static String BASE_URL = "http://10.0.2.2:3001";
    @GET("api/jobs")
    Call<ArrayList<Job>> getListJobs();
}
