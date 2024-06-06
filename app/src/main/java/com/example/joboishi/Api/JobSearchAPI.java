package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.Models.JobSearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JobSearchAPI {

    String BASE_URL = APIURL.BASE_URL;

    @GET("api/jobs/search")
    Call<ArrayList<JobBasic>> getListSearchJob(
            @Query("q") String keyword,
            @Query("remote") Boolean remote,
            @Query("experience") String experience,
            @Query("jobType") String jobType,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    // Lấy sang Chọn ngành nghề
    @GET("api/jobs/keyword")
    Call<ArrayList<String>> getListJobs();
}
