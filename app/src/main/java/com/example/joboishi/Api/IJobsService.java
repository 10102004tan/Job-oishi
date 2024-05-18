package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IJobsService {

    public static String BASE_URL = "http://ip172-18-0-7-cp48j2ol2o9000avmmug-3001.direct.labs.play-with-docker.com/";

    @POST("api/jobs")
    Call<ArrayList<JobBasic>> getListJobs(@Query("page") int page);

    @POST("api/jobs/bookmark")
    Call<JobBasic> addJobToBookmark(@Body JobBasic jobBasic);

    @DELETE("api/jobs/bookmark/destroy")
    Call<ResponseBody> destroyJobOnBookmark(@Query("id") int id, @Query("userId") int userId);

    @POST("api/jobs/bookmark/all")
    Call<ArrayList<JobBasic>> getAllJobsBookmarkById(@Body int userId);
}
