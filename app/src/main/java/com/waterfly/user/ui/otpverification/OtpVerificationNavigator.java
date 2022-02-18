package com.waterfly.user.ui.otpverification;

public interface OtpVerificationNavigator {

    void handleError(Throwable throwable);

    void login();

    void handleMassages(String massage);

    void openUserDetailsActivity();

    void backToEditNumber();

    void openMainActivity();

    void resendOTP();
}
