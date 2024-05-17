package com.example.joboishi.Api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @GET("{id}")
    Call<UserApiResponse> getDetailUser(@Path("id") int userId);

    @POST("{id}")
    Call<UserApiResponse> updateUserInfo(@Path("id") int userId, @Body UserRequest userUpdateRequest);

    @Multipart
    @POST("{id}")
    Call<UserApiResponse> updateAvatar(@Path("id") int userId, @Part MultipartBody.Part photo_url);

    @POST("/api/user")
    Call<UserApiResponse> registerUser(@Body UserRegisterEmailRequest request);
  
    @POST("/api/user/login")
    Call<UserApiResponse> loginUser(@Body UserLoginEmailRequest request);

    @POST("{id}/job_criteria")
    Call<JobCriteriaApiResponse> updateJobCriteria(@Body JobCriteriaRequest request);

    @GET("{id}/job_criteria")
    Call<JobCriteriaApiResponse> getJobCriteria(@Path("id") int userId);
}

