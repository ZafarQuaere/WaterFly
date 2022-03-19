package com.waterfly.user.ui.main;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.libraries.places.api.model.Place;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;
import com.waterfly.user.ui.base.BaseViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerViewModel extends BaseViewModel<BannerScreenNavigator> {
    private NearByVendorsResponse nearByVendorsResponse;
    public BannerViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public void onClick() {
        getNavigator().openAutoCompleteSearch();
    }

    public void onUserIconClick() {
        if(nearByVendorsResponse !=null)
        getNavigator().openUserDetailsPopUp(nearByVendorsResponse);
    }

    public void openFullMapView(Place place){
//        if(nearByVendorsResponse !=null)
        getNavigator().openFullMapView(nearByVendorsResponse,place);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getNearByVendorDetails(double lat,double lng) {
        setIsLoading(true);
        getDataManager().getUserService().getApiService().getNearByVendorDetails(getDataManager().getSharedPreference().getUserId(),lat,lng).enqueue(new BannerViewModel.NearByVendors());
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
             nearByVendorsResponse = response.body();
            setIsLoading(false);

//            if (nearByVendorsResponse != null && nearByVendorsResponse.getStatus() == 1) {
                    setNearByVendorDetailsOnMap(nearByVendorsResponse);
//            } else {
//                setIsLoading(false);
//                getNavigator().handleMassages(AppValidationMassages.DATA_BLANK);
//            }
        }

        @Override
        public void onFailure(Call<NearByVendorsResponse> call, Throwable throwable) {
            setIsLoading(false);
            getNavigator().handleError(throwable);
        }
    }


}
