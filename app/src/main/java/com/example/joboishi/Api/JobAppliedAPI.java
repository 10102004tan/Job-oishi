package com.example.joboishi.Api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JobAppliedAPI {
    String BASE_URL = "http://ip172-18-0-43-cp43q3iim2rg00aoeki0-3001.direct.labs.play-with-docker.com/";

    @Multipart
    @POST(BASE_URL + "api/applied")
    Call<ResponseBody> applied(
            @Part("id") RequestBody id,
            @Part("user_id") RequestBody user_id,
            @Part("title") RequestBody title,
            @Part("company_id") RequestBody company_id,
            @Part("company_logo") RequestBody company_logo,
            @Part("sort_addresses") RequestBody sort_addresses,
            @Part("is_applied") RequestBody is_applied,
            @Part("is_salary_visible") RequestBody is_salary_visible,
            @Part("published") RequestBody published
    );
}
