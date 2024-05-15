package com.example.joboishi.Api;

import com.example.joboishi.Models.JobSearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JobSearchAPI {
    public static String BASE_URL = "http://10.0.2.2:3001";

    @GET("api/jobs/search")
    Call<ArrayList<JobSearch>> getListSearchJob(
            @Query("q") String keyword,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    @GET("api/jobs")
    Call<ArrayList<JobSearch>> getListJobs();

    @GET("api/jobs2")
    Call<ArrayList<JobSearch>> getListSearchJobAll(
            @Query("page") int page,
            @Query("page_size") int pageSize
    );
}
