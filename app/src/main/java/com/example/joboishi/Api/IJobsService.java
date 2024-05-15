package com.example.joboishi.Api;

import com.example.joboishi.Models.Job;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.Models.JobSearch;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IJobsService {
    public static String BASE_URL = "http://10.0.2.2:3001";
    @GET("api/jobs")
    Call<ArrayList<JobBasic>> getListJobs();

    @POST("api/jobs/bookmark")
    Call<JobBasic> addJobToBookmark(@Body JobBasic jobBasic);

    @POST("api/jobs/bookmark/all")
    Call<ArrayList<JobBasic>> getAllJobsBookmarkById(@Body int userId);
}
