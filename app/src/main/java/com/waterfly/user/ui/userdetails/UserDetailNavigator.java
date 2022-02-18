package com.waterfly.user.ui.userdetails;

public interface UserDetailNavigator {

    void handleError(Throwable throwable);

    void sendOtp();

    void handleMassages(String massage);

    void openMainDashboardActivity();
}
