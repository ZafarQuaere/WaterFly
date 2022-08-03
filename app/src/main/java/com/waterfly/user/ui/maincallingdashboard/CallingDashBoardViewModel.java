package com.waterfly.user.ui.maincallingdashboard;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableBoolean;

import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;
import com.waterfly.user.ui.base.BaseViewModel;
import com.waterfly.user.utils.DialogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallingDashBoardViewModel extends BaseViewModel<CallingDashboardNavigator> {

    private final ObservableBoolean mIsLoading = new ObservableBoolean();

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }

    public CallingDashBoardViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public void onClick() {
        getNavigator().openAutoCompleteSearch();
    }

    public void onCallClick() {
        getNavigator().onCallClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getNearByVendorDetails(double lat,double lng) {
        setIsLoading(true);
        getDataManager().getUserService().getApiService().getNearByVendorDetails(getDataManager().getSharedPreference().getUserId(),lat,lng).enqueue(new NearByVendors());
    }
    private void setNearByVendorDetailsOnMap(NearByVendorsResponse nearByVendorsResponse){
            getNavigator().nearByVendorDetails(nearByVendorsResponse);
    }

    /**
     * Callback
     **/
    private class NearByVendors implements Callback<NearByVendorsResponse> {

        @Override
        public void onResponse(@NonNull Call<NearByVendorsResponse> call, @NonNull Response<NearByVendorsResponse> response) {
            NearByVendorsResponse nearByVendorsResponse = response.body();
            setIsLoading(false);
            if (nearByVendorsResponse != null && nearByVendorsResponse.getStatus() == 1) {
                    setNearByVendorDetailsOnMap(nearByVendorsResponse);
            } else {
                setIsLoading(false);
                setNearByVendorDetailsOnMap(null);
                getNavigator().handleMassages(nearByVendorsResponse.getMessage().get(0));
            }
        }

        @Override
        public void onFailure(Call<NearByVendorsResponse> call, Throwable throwable) {
            setIsLoading(false);
            getNavigator().handleError(throwable);
        }
    }

    public void onCallRefresh(){
        getNavigator().onCallRefresh();
    }
}
