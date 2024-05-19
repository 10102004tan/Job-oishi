package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IJobsService {

    String BASE_URL = APIURL.BASE_URL;
    @GET("api/jobs2")
    Call<ArrayList<JobBasic>> getListJobsDB(@Query("page") int page, @Query("user_id") int user_id);

    @POST("api/jobs")
    Call<ArrayList<JobBasic>> getListJobs(@Query("page") int page, @Query("user_id") int user_id);
    @POST("api/applied-job")
    Call<ArrayList<JobBasic>> getJobApplied(@Query("id") int id);

}
