package com.example.joboishi.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static final String COUNTRY_URL = "https://restcountries.com/v3.1/";
    static final String PROVINCE_URL = "https://vietnam-administrative-division-json-server-swart.vercel.app/";
    static final String USER_URL = "http://ip172-18-0-93-cp7diq2im2rg00celnpg-3001.direct.labs.play-with-docker.com/api/user/";

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getApiClient(String apiUrl) {
        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static CountryApi getCountryAPI() {
        return getApiClient(COUNTRY_URL).create(CountryApi.class);
    }

    public static ProvinceApi getProvinceAPI() {
        return getApiClient(PROVINCE_URL).create(ProvinceApi.class);
    }

    public static UserApi getUserAPI() {
        return getApiClient(USER_URL).create(UserApi.class);
    }
}
