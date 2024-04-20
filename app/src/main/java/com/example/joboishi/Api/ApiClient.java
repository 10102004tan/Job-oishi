package com.example.joboishi.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static final String COUNTRY_URL = "https://restcountries.com/v3.1/";
    static final String PROVINCE_URL = "https://vietnam-administrative-division-json-server-swart.vercel.app/";

    public static Retrofit getApiClient(String apiUrl) {
        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static CountryApi getCountryAPI() {
        return getApiClient(COUNTRY_URL).create(CountryApi.class);
    }

    public static ProvinceApi getProvinceAPI() {
        return getApiClient(PROVINCE_URL).create(ProvinceApi.class);
    }
}
