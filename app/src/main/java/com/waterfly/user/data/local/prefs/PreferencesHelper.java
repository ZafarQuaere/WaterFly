package com.waterfly.user.data.local.prefs;


public interface PreferencesHelper {

    String getAccessToken();

    void setAccessToken(String accessToken);

    void setUserId(String user_id);

    String getUserId();
}
