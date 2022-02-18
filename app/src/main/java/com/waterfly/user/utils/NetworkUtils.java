package com.waterfly.user.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public final class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}