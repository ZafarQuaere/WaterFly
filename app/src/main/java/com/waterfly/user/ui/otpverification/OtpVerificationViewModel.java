package com.waterfly.user.ui.otpverification;

import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.network.model.SendOtpResponse;
import com.waterfly.user.data.network.model.verifiedOtpResponse.OtpVerifiedResponse;
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


public class OtpVerificationViewModel extends BaseViewModel<OtpVerificationNavigator> {

    public MutableLiveData<String> token = new MutableLiveData<>();


    public OtpVerificationViewModel(DataManager dataManager) {
        super(dataManager);
    }


    public void onClick() {
        getNavigator().login();
    }

    public void onEditClick(){
        getNavigator().backToEditNumber();
    }

    public void onClickResend(){
        getNavigator().resendOTP();
    }

    public boolean isOTPValid(String otp) {
        // validate email and password
        if (TextUtils.isEmpty(otp)) {
            getNavigator().handleMassages(AppValidationMassages.EMPTY_OTP);
            return false;
        }
        if (!CommonUtils.isValidOTP(otp)) {
            getNavigator().handleMassages(AppValidationMassages.INVALID_OTP);
            return false;
        }
        getNavigator().handleMassages("");
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void otpVerification(String otp,String mobileNumber) {
            setIsLoading(true);
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put(AppConstants.ACTION,AppConstants.VALIDATE_OTP);
            jsonParams.put(AppConstants.PHONE,mobileNumber);
            jsonParams.put(AppConstants.OTP,otp);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
            getDataManager().getUserService().getApiService().verifyOtp(body).enqueue(new OtpVerifiedCallback());
    }

    private void setLoginDetails(OtpVerifiedResponse otpVerifiedResponse) {
        getDataManager().getSharedPreference().setAccessToken(otpVerifiedResponse.getData().get(0).getJWTToken());
//        getDataManager().getSharedPreference().setUserId(otpVerifiedResponse.getData().get(0).getId());
        getDataManager().getSharedPreference().setUserId(otpVerifiedResponse.getData().get(0).getId());
        getDataManager().getSharedPreference().setUserId(otpVerifiedResponse.getData().get(0).getId());
        getDataManager().getSharedPreference().setUserName(otpVerifiedResponse.getData().get(0).getName());
        getDataManager().getSharedPreference().setUserPhone(otpVerifiedResponse.getData().get(0).getPhone());

        if(otpVerifiedResponse.getData().get(0).getDetailsCompleted().equalsIgnoreCase("0")){
            getNavigator().openUserDetailsActivity();
        }else{
            getNavigator().openMainActivity();
        }
    }

    /**
     * Callback
     **/
    private class OtpVerifiedCallback implements Callback<OtpVerifiedResponse> {

        @Override
        public void onResponse(@NonNull Call<OtpVerifiedResponse> call, @NonNull Response<OtpVerifiedResponse> response) {
            setIsLoading(false);
            OtpVerifiedResponse otpVerifiedResponse = response.body();
            if (otpVerifiedResponse != null) {
                if(otpVerifiedResponse.getStatus() == 1) {
                    setLoginDetails(otpVerifiedResponse);
                }else{
                    getNavigator().handleMassages(AppValidationMassages.INVALID_OTP);
                }
            } else {
                getNavigator().handleMassages(AppValidationMassages.INVALID_OTP);
            }
        }

        @Override
        public void onFailure(Call<OtpVerifiedResponse> call, Throwable throwable) {
            setIsLoading(false);
                getNavigator().handleError(throwable);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendOtp(String mobileNumber) {
        setIsResendLoading(true);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put(AppConstants.ACTION,AppConstants.REQUEST_OTP);
        jsonParams.put(AppConstants.PHONE,mobileNumber);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"),(new JSONObject(jsonParams)).toString());
        getDataManager().getUserService().getApiService().sendOtp(body).enqueue(new OtpVerificationViewModel.SendOtpCallback());
    }

    private void callOtpVerificationScreen() {
//        getNavigator().openOtpVerificationActivity();
    }

    /**
     * Callback
     **/
    private class SendOtpCallback implements Callback<SendOtpResponse> {

        @Override
        public void onResponse(@NonNull Call<SendOtpResponse> call, @NonNull Response<SendOtpResponse> response) {
            SendOtpResponse sendOtpResponse = response.body();
            setIsResendLoading(false);
//            if (sendOtpResponse != null) {
//                if(sendOtpResponse.getStatus() == 1){
//
//                }
//            } else {
//                setIsLoading(false);
//                getNavigator().handleMassages(AppValidationMassages.DATA_BLANK);
//            }
        }

        @Override
        public void onFailure(Call<SendOtpResponse> call, Throwable throwable) {
            setIsResendLoading(false);
            getNavigator().handleError(throwable);
        }
    }

}
