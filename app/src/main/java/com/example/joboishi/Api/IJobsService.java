package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IJobsService {

    public static String BASE_URL = "http://10.0.2.2:3001";

    @POST("api/jobs")
    Call<ArrayList<JobBasic>> getListJobs(@Query("page") int page);

    @GET("api/jobs2")
    Call<ArrayList<JobBasic>> getListJobsDB(@Query("page") int page);

    @POST("api/jobs/bookmark/all")
    Call<ArrayList<JobBasic>> getAllJobsBookmarkById(@Body int userId);

    @POST("api/applied")
    Call<ArrayList<JobBasic>> getJobApplied();
}
