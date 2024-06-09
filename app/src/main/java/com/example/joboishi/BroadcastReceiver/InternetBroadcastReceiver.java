package com.example.joboishi.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.widget.Toast;

public class InternetBroadcastReceiver extends BroadcastReceiver {
    public IInternetBroadcastReceiverListener listener;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

        if (networkCapabilities != null) {
            boolean isInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            boolean isNotMetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);

            if (isInternet && !isNotMetered) {
                listener.lowInternet();
            } else {
                listener.goodInternet();
            }
        }
        else{
            listener.noInternet();
        }
    }

    public interface IInternetBroadcastReceiverListener {
       void noInternet();
         void lowInternet();
         void goodInternet();
    }
}
