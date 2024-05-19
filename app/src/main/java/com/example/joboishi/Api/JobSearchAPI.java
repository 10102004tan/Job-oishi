package com.example.joboishi.Api;

import com.example.joboishi.Models.JobSearch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JobSearchAPI {
    String BASE_URL = "http://ip172-18-0-43-cp43q3iim2rg00aoeki0z-3001.direct.labs.play-with-docker.com/";

    @GET("api/jobs/search")
    Call<ArrayList<JobSearch>> getListSearchJob(
            @Query("q") String keyword,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    @GET("api/jobs/searchremote")
    Call<ArrayList<JobSearch>> getListSearchRmJob(
            @Query("q") String keyword,
            @Query("page") int page,
            @Query("page_size") int pageSize
    );

    // Lấy bỏ sang Chọn ngành nghề
    @GET("api/sjobs")
    Call<ArrayList<JobSearch>> getListJobs();

    @GET("api/jobs")
    Call<ArrayList<JobSearch>> getListSearchJobAll(
            @Query("page") int page,
            @Query("page_size") int pageSize
    );
}
