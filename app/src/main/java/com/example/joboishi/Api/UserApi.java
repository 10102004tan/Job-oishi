package com.example.joboishi.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @GET("{id}")
    Call<UserApiResponse> getDetailUser(@Path("id") int userId);

    @POST("{id}")
    Call<UserApiResponse> updateUserInfo(@Path("id") int userId ,@Body UserRequest userUpdateRequest);
}
