package com.waterfly.user.ui.userdetails;

import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.UserDetailsResponse;
import com.waterfly.user.ui.base.BaseViewModel;
import com.waterfly.user.utils.AppConstants;
import com.waterfly.user.utils.AppValidationMassages;
import com.waterfly.user.utils.CommonUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserDetailViewModel extends BaseViewModel<UserDetailNavigator> {

    public MutableLiveData<String> token = new MutableLiveData<>();

    public UserDetailViewModel(DataManager dataManager) {
        super(dataManager);
    }

    public void onClick() {
        getNavigator().sendOtp();
    }

    public boolean isUserDetailsValid(String userInfo,String email) {
        // validate email and password
        if (TextUtils.isEmpty(userInfo)) {
            getNavigator().handleMassages(AppValidationMassages.EMPTY_USER_NAME);
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            getNavigator().handleMassages(AppValidationMassages.EMPTY_USER_EMAIL);
            return false;
        }
        if (!CommonUtils.isValidUserName(userInfo)) {
            getNavigator().handleMassages(AppValidationMassages.INVALID_USER_NAME);
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            getNavigator().handleMassages(AppValidationMassages.INVALID_EMAIL);
            return false;
        }

        getNavigator().handleMassages("");
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveUserDetails(String txtFullName,String txtEmail) {
            setIsLoading(true);
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put(AppConstants.JWT_TOKEN,getDataManager().getSharedPreference().getAccessToken());
                jsonParams.put(AppConstants.USER_ID,getDataManager().getSharedPreference().getUserId());
                jsonParams.put(AppConstants.NAME,txtFullName);
                jsonParams.put(AppConstants.EMAIL,txtEmail);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
                getDataManager().getUserService().getApiService().saveUserDetails(body).enqueue(new UserDetailsCallback());

    }

    private void callMainDashBoardScreen() {
        getNavigator().openMainDashboardActivity();
    }

    /**
     * Callback
     **/
    private class UserDetailsCallback implements Callback<UserDetailsResponse> {

        @Override
        public void onResponse(@NonNull Call<UserDetailsResponse> call, @NonNull Response<UserDetailsResponse> response) {
            UserDetailsResponse userDetailsResponse = response.body();
            setIsLoading(false);
            if (userDetailsResponse != null) {
                if(userDetailsResponse.getStatus() == 1){
                    callMainDashBoardScreen();
                }
            } else {
                getNavigator().handleMassages(AppValidationMassages.DATA_BLANK);
            }
        }

        @Override
        public void onFailure(Call<UserDetailsResponse> call, Throwable throwable) {
            setIsLoading(false);
            getNavigator().handleError(throwable);
        }
    }

}
