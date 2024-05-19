package com.example.joboishi.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserFcmAPI {

    String BASE_URL = APIURL.BASE_URL;

    // Gửi userId va token lên server để xác thực
    @POST("api/fcm")
    Call<ResponseBody> sendFcmToken(@Query("user_id") int userId, @Query("fcm_token") String fcmToken);
}
