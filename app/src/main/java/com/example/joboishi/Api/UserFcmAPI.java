package com.example.joboishi.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserFcmAPI {
    String BASE_URL = "http://ip172-18-0-88-cp4c4j8l2o9000avmqv0-3001.direct.labs.play-with-docker.com/";

    // Gửi userId va token lên server để xác thực
    @POST("/api/fcm")
    Call<ResponseBody> sendFcmToken(@Query("user_id") int userId, @Query("fcm_token") String fcmToken);
}
