package com.example.joboishi.Abstracts;

import android.content.Context;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;

public abstract class BaseActivity extends AppCompatActivity {
    protected InternetBroadcastReceiver internetBroadcastReceiver;
    protected IntentFilter intentFilter;

    @Override
    public void onStart() {
        super.onStart();
        registerInternetBroadcastReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetBroadcastReceiver);
    }


    protected void registerInternetBroadcastReceiver() {
        internetBroadcastReceiver = new InternetBroadcastReceiver();
        internetBroadcastReceiver.listener = new InternetBroadcastReceiver.IInternetBroadcastReceiverListener() {
            @Override
            public void noInternet() {
                handleNoInternet();
            }

            @Override
            public void lowInternet() {
                handleLowInternet();
            }
            @Override
            public void goodInternet() {
                handleGoodInternet();
            }
        };
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }


    protected abstract void handleNoInternet(); // Xử lý khi không có internet
    protected abstract void handleLowInternet(); // Xử lý khi mạng yếu
    protected abstract void handleGoodInternet(); // Xử lý khi có mạng
}
