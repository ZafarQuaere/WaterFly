package com.waterfly.user.ui.base;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;


import com.waterfly.user.data.DataManager;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {

    private final DataManager mDataManager;

    private final ObservableBoolean mIsResendLoading = new ObservableBoolean();

    public BaseViewModel(DataManager dataManager) {
        this.mDataManager= dataManager;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    private WeakReference<N> mNavigator;

    private final ObservableBoolean mIsLoading = new ObservableBoolean();

    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }

    public ObservableBoolean getIsResendLoading() {
        return mIsResendLoading;
    }

    public void setIsResendLoading(boolean isLoading) {
        mIsResendLoading.set(isLoading);
    }

}
