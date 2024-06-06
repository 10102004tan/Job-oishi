package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.Models.data.Job;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IJobsService {

    String BASE_URL = APIURL.BASE_URL;
    @GET("api/jobs")
    Call<JobsResponse> getListJobs(@Query("city") String city, @Query("page") int page, @Query("user_id") int user_id,@Query("type")int type);
    @POST("api/applied-job")
    Call<ArrayList<JobBasic>> getJobApplied(@Query("id") int id);
    @GET("api/bookmarks")
    Call<JobBookmarksResponse> getJobBookmark(@Query("user_id") int userId,@Query("page") int page);
    @GET("api/bookmarks/ids")
    Call<ArrayList<Integer>> getIdsJobBookmark(@Query("user_id") int userId);
    @POST("api/bookmarks/store")
    Call<Void> addBookmark(@Query("user_id") int userId,@Query("job_id") int jobId,@Query("type") int type);
    @POST("api/bookmarks/destroy")
    Call<Void> destroyBookmark(@Query("user_id") int userId,@Query("job_id") int jobId,@Query("type") int type);
    @GET("api/bookmarks/total")
    Call<Integer> getTotalBookmark(@Query("user_id") int userId);
    @GET("api/applied/total")
    Call<Integer> getTotalJobsApplied(@Query("user_id") int userId);
    @GET("api/jobs/detail")
    Call<Job> getJobDetail(@Query("job_id") int jobId,@Query("type") int type,@Query("user_id") int user_id);
}
