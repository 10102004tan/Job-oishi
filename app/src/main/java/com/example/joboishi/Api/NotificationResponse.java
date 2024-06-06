package com.example.joboishi.Api;

import com.example.joboishi.Models.Notification;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponse {

    @SerializedName("notifications")
    private ArrayList<Notification> notifications;

    @SerializedName("unreadCount")
    private int totalNotRead;

    public NotificationResponse(ArrayList<Notification> notifications, int totalNotRead) {
        this.notifications = notifications;
        this.totalNotRead = totalNotRead;
    }

    public NotificationResponse(){
        notifications = new ArrayList<>();
        totalNotRead = 0;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public int getTotalNotRead() {
        return totalNotRead;
    }

    public void setTotalNotRead(int totalNotRead) {
        this.totalNotRead = totalNotRead;
    }
}
