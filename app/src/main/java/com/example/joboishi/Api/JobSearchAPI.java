package com.example.joboishi.Api;

import com.example.joboishi.Models.JobSearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JobSearchAPI {

    public static String BASE_URL = "http://ip172-18-0-7-cp48j2ol2o9000avmmug-3001.direct.labs.play-with-docker.com/";

    @GET("api/sjobs/search")
    Call<ArrayList<JobSearch>> getListSearchJob(
            @Query("q") String keyword,
            @Query("remote") Boolean remote,
            @Query("experience") String experience,
            @Query("jobType") String jobType,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    // Lấy bỏ sang Chọn ngành nghề
    @GET("api/sjobs")
    Call<ArrayList<JobSearch>> getListJobs();

}
