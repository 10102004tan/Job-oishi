package com.example.joboishi.Api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @POST("/api/user/facebook")
    Call<UserApiResponse> registerFacebookUser(@Body UserFacebookLoginRequest request);
  
    @POST("/api/user/login")
    Call<UserApiResponse> loginUser(@Body UserLoginEmailRequest request);

    @POST("/api/user/{id}/job_criteria")
    Call<JobCriteriaApiResponse> updateJobCriteria(@Body JobCriteriaRequest request);

    @GET("/api/user/{id}/job_criteria")
    Call<JobCriteriaApiResponse> getJobCriteria(@Path("id") int userId);

    @FormUrlEncoded
    @POST("/api/forgot-password")
    Call<ForgotPasswordApiResponse> forgotPassword(@Field("email") String email);

    @POST("/api/reset-password")
    Call<ForgotPasswordApiResponse> resetPassword(@Body UserResetPasswordRequest request);

    @POST("/api/verify-token")
    Call<ForgotPasswordApiResponse> verifyPassword(@Body UserVerifyTokenRequest request);
}

