package com.waterfly.user.ui.maincallingdashboard;

import android.os.Build;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableBoolean;

import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;
import com.waterfly.user.data.network.model.nearbyvendors.UserCallLogResponse;
import com.waterfly.user.ui.base.BaseViewModel;
import com.waterfly.user.ui.userdetails.UserDetailViewModel;
import com.waterfly.user.utils.AppConstants;
import com.waterfly.user.utils.DialogUtil;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void userCallLogApi(String vendorId) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put(AppConstants.JWT_TOKEN,getDataManager().getSharedPreference().getAccessToken());
        jsonParams.put(AppConstants.USER_ID,getDataManager().getSharedPreference().getUserId());
        jsonParams.put(AppConstants.VENDOR_ID,vendorId);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        getDataManager().getUserService().getApiService().userCallLogApi(body).enqueue(new UserCallLogCallback());
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

    /**
     * Callback
     **/
    private class UserCallLogCallback implements Callback<UserCallLogResponse> {

        @Override
        public void onResponse(@NonNull Call<UserCallLogResponse> call, @NonNull Response<UserCallLogResponse> response) {
            UserCallLogResponse nearByVendorsResponse = response.body();
            DialogUtil.DEBUG("User Call Log Response: >> "+nearByVendorsResponse.getMessage().get(0));
        }

        @Override
        public void onFailure(Call<UserCallLogResponse> call, Throwable throwable) {
            getNavigator().handleError(throwable);
        }
    }

    public void onCallRefresh(){
        getNavigator().onCallRefresh();
    }
}
