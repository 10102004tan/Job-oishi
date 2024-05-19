package com.example.joboishi.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static final String COUNTRY_URL = "https://restcountries.com/v3.1/";
    static final String PROVINCE_URL = "https://vietnam-administrative-division-json-server-swart.vercel.app/";
    static final String USER_URL = "http://10.0.2.2:3001/api/user/";

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
