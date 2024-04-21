package com.example.joboishi.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryApi {
    @GET("all")
    Call<ArrayList<com.example.joboishi.Api.CountryApiResponse>> getData();
}
