package com.example.joboishi.Api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPI {
    public static String BASE_URL = "http://ip172-18-0-7-cp48j2ol2o9000avmmug-3001.direct.labs.play-with-docker.com/";

    @Multipart
    @POST(BASE_URL + "api/upload")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file,
            @Part("user_id")RequestBody userId
    );
}
