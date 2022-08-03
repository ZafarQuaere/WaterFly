package com.waterfly.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.waterfly.user.data.DataManager;
import com.waterfly.user.ui.banner.BannerViewModel;
import com.waterfly.user.ui.maincallingdashboard.CallingDashBoardViewModel;
import com.waterfly.user.ui.login.LoginViewModel;
import com.waterfly.user.ui.otpverification.OtpVerificationViewModel;
import com.waterfly.user.ui.splash.SplashViewModel;
import com.waterfly.user.ui.userdetails.UserDetailViewModel;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private final DataManager mDataManager;

    public ViewModelFactory(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BannerViewModel.class)) {
            return (T) new BannerViewModel(mDataManager);
        }else if(modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mDataManager);
        }else if(modelClass.isAssignableFrom(SplashViewModel.class)) {
            return (T) new SplashViewModel(mDataManager);
        }else if(modelClass.isAssignableFrom(OtpVerificationViewModel.class)) {
            return (T) new OtpVerificationViewModel(mDataManager);
        }else if(modelClass.isAssignableFrom(UserDetailViewModel.class)) {
            return (T) new UserDetailViewModel(mDataManager);
        }else if(modelClass.isAssignableFrom(CallingDashBoardViewModel.class)) {
            return (T) new CallingDashBoardViewModel(mDataManager);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
