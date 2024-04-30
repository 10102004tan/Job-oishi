package com.example.joboishi.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProvinceApi {
    @GET("province")
    Call<ArrayList<ProvinceApiResponse>> getData();
}
