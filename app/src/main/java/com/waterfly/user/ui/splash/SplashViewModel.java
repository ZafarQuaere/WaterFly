

package com.waterfly.user.ui.splash;

import android.os.Handler;

import com.waterfly.user.data.DataManager;
import com.waterfly.user.ui.base.BaseViewModel;


public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public void startSeeding() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                decideNextActivity();
            }
        },2000);
    }

    private void decideNextActivity() {
        if (getDataManager().getSharedPreference().getAccessToken() == null) {
            getNavigator().openLoginActivity();
        } else {
            getNavigator().openMainActivity();
        }
    }
}