package com.example.joboishi.Api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JobAppliedAPI{
    public static String BASE_URL = "http://10.0.2.2:3001";

    @Multipart
    @POST(BASE_URL + "api/applied")
    Call<ResponseBody> applied(
            @Part("job_id") RequestBody job_id,
            @Part("user_id") RequestBody user_id
    );
}
