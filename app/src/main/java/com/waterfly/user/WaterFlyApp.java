package com.waterfly.user;
import android.app.Application;
import com.waterfly.user.utils.AppLogger;

public class WaterFlyApp extends Application{

    private static WaterFlyApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        AppLogger.init();
    }

    public static WaterFlyApp getInstance() {
        return sInstance;
    }

}