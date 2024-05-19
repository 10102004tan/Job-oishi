package com.example.joboishi.Api;

import com.example.joboishi.Models.data.FileCV;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UploadAPI {
    public static String BASE_URL = APIURL.BASE_URL;

    //Thêm file
    @Multipart
    @POST(BASE_URL + "api/upload")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody userId,
            @Part("file_name") RequestBody file_name,
            @Part("file_size") RequestBody file_size,
            @Part("upload_at") RequestBody upload_at
    );

    //Lay file mà user đã upload
    @GET("api/upload/{user_id}")
    Call<FileCV> getFile(@Path("user_id") String user_id);

    //Xóa file mà user đã upload
    @DELETE("api/upload/{id}")
    Call<ResponseBody> deleteFile(@Path("id") String id);
}

