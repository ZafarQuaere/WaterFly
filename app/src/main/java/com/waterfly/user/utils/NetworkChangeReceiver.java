package com.waterfly.user.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkChangeReceiver extends BroadcastReceiver
{

    public NetworkChangeReceiver(OnEventListener callback) {
        this.mOnEventListener = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
                mOnEventListener.onDialog(true);
                Log.e("keshav", "Online Connect Intenet ");
            } else {
                mOnEventListener.onDialog(false);
                Log.e("keshav", "Conectivity Failure !!! ");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private OnEventListener mOnEventListener;


    public interface OnEventListener {
        void onDialog(boolean value);
        // or void onEvent(); as per your need
    }
}
