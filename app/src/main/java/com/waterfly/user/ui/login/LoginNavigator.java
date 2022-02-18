package com.waterfly.user.ui.login;

public interface LoginNavigator {

    void handleError(Throwable throwable);

    void sendOtp();

    void handleMassages(String massage);

    void openOtpVerificationActivity();
}
