package com.waterfly.user.ui.login;

import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.SendOtpResponse;
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


public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    public MutableLiveData<String> token = new MutableLiveData<>();


    public LoginViewModel(DataManager dataManager) {
        super(dataManager);
    }


    public void onClick() {
        getNavigator().sendOtp();
    }

    public boolean isEmailAndPasswordValid(String mobileNumber) {
        // validate email and password
        if (TextUtils.isEmpty(mobileNumber)) {
            getNavigator().handleMassages(AppValidationMassages.EMPTY_MOBILE_NUMBER);
            return false;
        }
        if (!CommonUtils.isPhoneNumberValid(mobileNumber)) {
            getNavigator().handleMassages(AppValidationMassages.INVALID_MOBILE_NUMBER);
            return false;
        }
        getNavigator().handleMassages("");
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendOtp(String mobileNumber) {
            setIsLoading(true);
           Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put(AppConstants.ACTION,AppConstants.REQUEST_OTP);
            jsonParams.put(AppConstants.PHONE,mobileNumber);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"),(new JSONObject(jsonParams)).toString());
            getDataManager().getUserService().getApiService().sendOtp(body).enqueue(new SendOtpCallback());
    }

    private void callOtpVerificationScreen() {
        getNavigator().openOtpVerificationActivity();
    }

    /**
     * Callback
     **/
    private class SendOtpCallback implements Callback<SendOtpResponse> {

        @Override
        public void onResponse(@NonNull Call<SendOtpResponse> call, @NonNull Response<SendOtpResponse> response) {
            SendOtpResponse sendOtpResponse = response.body();
            setIsLoading(false);
            if (sendOtpResponse != null) {
                if(sendOtpResponse.getStatus() == 1){
                    callOtpVerificationScreen();
                }
            } else {
                setIsLoading(false);
                getNavigator().handleMassages(AppValidationMassages.DATA_BLANK);
            }
        }

        @Override
        public void onFailure(Call<SendOtpResponse> call, Throwable throwable) {
            setIsLoading(false);
            getNavigator().handleError(throwable);
        }
    }

}
