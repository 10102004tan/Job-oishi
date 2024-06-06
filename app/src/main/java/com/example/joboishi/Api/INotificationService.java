package com.example.joboishi.Api;

import com.example.joboishi.Models.Notification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INotificationService {
    String BASE_URL = APIURL.BASE_URL;
    @GET("api/getallnotifications")
    Call<NotificationResponse> getAllNotifications(@Query("user_id") int user_id);

    @GET("api/notificationreaded")
    Call<Void> readed(@Query("notification_id") int notification_id, @Query("user_id") int user_id);
}
