package com.example.joboishi.Api;

import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.Models.Keyword;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KeywordAPI {

    String BASE_URL = APIURL.BASE_URL;

    // Lấy sang Chọn ngành nghề
    @GET("api/jobs/key")
    Call<ArrayList<Keyword>> getListKeyword();

}
