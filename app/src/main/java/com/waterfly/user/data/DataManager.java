package com.waterfly.user.data;

import com.waterfly.user.data.local.db.AppDbHelper;
import com.waterfly.user.data.local.prefs.AppPreferencesHelper;
import com.waterfly.user.data.network.services.POCService;

public class DataManager {

    private static DataManager sInstance;

    public static synchronized DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    public POCService getUserService() {
        return POCService.getInstance();
    }

    public AppPreferencesHelper getSharedPreference(){
        return  AppPreferencesHelper.getInstance();
    }

    public AppDbHelper getAppDbHelper(){
        return  AppDbHelper.getInstance();
    }

}
