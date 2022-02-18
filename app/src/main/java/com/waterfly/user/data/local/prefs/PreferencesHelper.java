package com.waterfly.user.data.local.prefs;


import com.waterfly.user.data.network.model.verifiedOtpResponse.OtpVerifiedResponse;

public interface PreferencesHelper {

    String getAccessToken();

    void setAccessToken(String accessToken);

    void setUserId(String user_id);

    String getUserId();
}
