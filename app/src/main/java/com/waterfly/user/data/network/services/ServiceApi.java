package com.waterfly.user.data.network.services;


import com.waterfly.user.data.network.ApiConstants;
import com.waterfly.user.data.network.model.LoginResponse;
import com.waterfly.user.data.network.model.SendOtpResponse;
import com.waterfly.user.data.network.model.UserDetailsResponse;
import com.waterfly.user.data.network.model.nearbyvendors.NearByVendorsResponse;
import com.waterfly.user.data.network.model.nearbyvendors.UserCallLogResponse;
import com.waterfly.user.data.network.model.verifiedOtpResponse.OtpVerifiedResponse;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {

    @GET(ApiConstants.USER_DETAILS)
    Call<UserDetailsResponse> getUserDetails();

    @POST(ApiConstants.SEND_OTP)
    Call<SendOtpResponse> sendOtp(@Body RequestBody body);

    @POST(ApiConstants.LOGIN)
    Call<LoginResponse> login(@Body RequestBody body);

    @POST(ApiConstants.SEND_OTP)
    Call<OtpVerifiedResponse> verifyOtp(@Body RequestBody body);

    @POST(ApiConstants.SAVE_USER_DETAILS)
    Call<UserDetailsResponse> saveUserDetails(@Body RequestBody body);

    @GET(ApiConstants.NEAR_BY_VENDOR)
    Call<NearByVendorsResponse> getNearByVendorDetails(@Query("user_id") String user_id,@Query("user_latitude") double user_latitude,@Query("user_longitude") double user_longitude);


    @POST(ApiConstants.USER_CALL_LOGS)
    Call<UserCallLogResponse> userCallLogApi(@Body RequestBody body);

}