package com.waterfly.user.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import com.waterfly.user.WaterFlyApp;
import com.waterfly.user.utils.AppConstants;

public class AppPreferencesHelper implements PreferencesHelper {


    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";
    private static final String PREF_KEY_USER_PHONE = "PREF_KEY_USER_PHONE";
    private static final String PREF_USER_ID = "PREF_USER_ID";
    private static AppPreferencesHelper instance = null;
    private final SharedPreferences mPrefs;


    public AppPreferencesHelper(Context context,String prefFileName) {
            mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public void setUserId(String user_id) {
        mPrefs.edit().putString(PREF_USER_ID, user_id).apply();
    }

    @Override
    public String getUserId() {
        return mPrefs.getString(PREF_USER_ID, null);
    }

    public static AppPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new AppPreferencesHelper(WaterFlyApp.getInstance(), AppConstants.PREF_NAME);
        }
        return instance;
    }


    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public void setUserPhone(String phone) {
        mPrefs.edit().putString(PREF_KEY_USER_PHONE, phone).apply();
    }

    @Override
    public String getUserPhone() {
        return mPrefs.getString(PREF_KEY_USER_PHONE, null);
    }

    @Override
    public void setUserName(String name) {
        mPrefs.edit().putString(PREF_KEY_USER_NAME, name).apply();
    }

    @Override
    public String getUserName() {
        return mPrefs.getString(PREF_KEY_USER_NAME, null);
    }

    public void clearAll() {
        mPrefs.edit().clear().apply();
    }
}
