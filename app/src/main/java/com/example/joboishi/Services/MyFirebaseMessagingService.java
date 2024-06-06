package com.example.joboishi.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.joboishi.Activities.HomeActivity;
import com.example.joboishi.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "1234567";

    private static OnMessageReceivedListener onMessageReceivedListener;

    public static void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        onMessageReceivedListener = listener;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();
        if (onMessageReceivedListener != null) {
            onMessageReceivedListener.onMessageReceived(title, body);
        }
        showNotification(title, body);
    }

    // show notification
    private void showNotification(String title, String body) {
        // Tạo intent để mở Activity khi người dùng click vào notification
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound);
        Intent intent = new Intent(this, HomeActivity.class); // Thay MainActivity bằng Activity bạn muốn mở
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Tạo notification builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(null)
                .setDefaults(0)
                ;

        // Tạo notification manager và hiển thị notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // chi xu y voi api > 28
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    public interface OnMessageReceivedListener{
        void onMessageReceived(String title, String body);
    }
}
