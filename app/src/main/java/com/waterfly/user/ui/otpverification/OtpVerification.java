package com.waterfly.user.ui.otpverification;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;
import com.waterfly.user.BR;
import com.waterfly.user.R;
import com.waterfly.user.ViewModelFactory;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.databinding.ActivityOtpVerifyBinding;
import com.waterfly.user.ui.base.BaseActivity;
import com.waterfly.user.ui.login.LoginActivity;
import com.waterfly.user.ui.main.BannerActivity;
import com.waterfly.user.ui.userdetails.UserDetailActivity;
import com.waterfly.user.utils.AppConstants;
import com.waterfly.user.utils.AppValidationMassages;


public class OtpVerification extends BaseActivity<ActivityOtpVerifyBinding, OtpVerificationViewModel> implements OtpVerificationNavigator {

    private ActivityOtpVerifyBinding mActivityOtpBinding;
    private OtpVerificationViewModel mOtpVerificationViewModel;
    private   String otp="";

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, OtpVerification.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.OtpVerificationViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otp_verify;
    }

    @Override
    public OtpVerificationViewModel getViewModel() {
        ViewModelFactory factory = new ViewModelFactory(DataManager.getInstance());
        mOtpVerificationViewModel = ViewModelProviders.of(this, factory).get(OtpVerificationViewModel.class);
        return mOtpVerificationViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityOtpBinding = getViewDataBinding();
        mOtpVerificationViewModel.setNavigator(this);
        mActivityOtpBinding.verifyMobileNumber.setText(getIntent().getStringExtra(AppConstants.PHONE));
        EditText[] edit = {mActivityOtpBinding.txtOtp1, mActivityOtpBinding.txtOtp2, mActivityOtpBinding.txtOtp3, mActivityOtpBinding.txtOtp4,mActivityOtpBinding.txtOtp5,mActivityOtpBinding.txtOtp6};
        mActivityOtpBinding.txtOtp1.addTextChangedListener(new GenericTextWatcher(mActivityOtpBinding.txtOtp1, edit));
        mActivityOtpBinding.txtOtp2.addTextChangedListener(new GenericTextWatcher(mActivityOtpBinding.txtOtp2, edit));
        mActivityOtpBinding.txtOtp3.addTextChangedListener(new GenericTextWatcher(mActivityOtpBinding.txtOtp3, edit));
        mActivityOtpBinding.txtOtp4.addTextChangedListener(new GenericTextWatcher(mActivityOtpBinding.txtOtp4, edit));
        mActivityOtpBinding.txtOtp5.addTextChangedListener(new GenericTextWatcher(mActivityOtpBinding.txtOtp5, edit));
        mActivityOtpBinding.txtOtp6.addTextChangedListener(new GenericTextWatcher(mActivityOtpBinding.txtOtp6, edit));

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mActivityOtpBinding.txtTimer.setText(" " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mActivityOtpBinding.txtTimer.setText("");
                mActivityOtpBinding.txtResend.setText("Resend");
                mActivityOtpBinding.btnResend.setClickable(true);
                mActivityOtpBinding.txtResend.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }.start();
    }

    @Override
    public void handleError(Throwable throwable) {}

    @Override
    public void handleMassages(String massage) {
        mActivityOtpBinding.txtErrorMassage.setText(massage);
    }

    private String getOTP(){
        if(mActivityOtpBinding.txtOtp1.getText().length() == 0 ||
                mActivityOtpBinding.txtOtp2.getText().length() == 0 ||
                mActivityOtpBinding.txtOtp3.getText().length() == 0 ||
                mActivityOtpBinding.txtOtp4.getText().length() == 0 ||
                mActivityOtpBinding.txtOtp5.getText().length() == 0 ||
                mActivityOtpBinding.txtOtp6.getText().length() == 0 ) {
            mActivityOtpBinding.txtErrorMassage.setText(AppValidationMassages.INVALID_OTP);
            return "";
        }


       return otp = mActivityOtpBinding.txtOtp1.getText().toString()+
                mActivityOtpBinding.txtOtp2.getText().toString()+
                mActivityOtpBinding.txtOtp3.getText().toString()+
                mActivityOtpBinding.txtOtp4.getText().toString()+
                mActivityOtpBinding.txtOtp5.getText().toString()+
                mActivityOtpBinding.txtOtp6.getText().toString();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void login() {
        callOtpVerificationAPI();
    }

    private void callOtpVerificationAPI(){
        String otp = getOTP();
        if (isNetworkConnected()) {
            if (mOtpVerificationViewModel.isOTPValid(otp)) {
                hideKeyboard();
                mOtpVerificationViewModel.otpVerification(otp,getIntent().getStringExtra(AppConstants.PHONE));
            }
        }else{
            mActivityOtpBinding.txtErrorMassage.setText(AppValidationMassages.No_INTERNET);
        }
    }

    @Override
    public void openUserDetailsActivity() {
        Intent intent = UserDetailActivity.newIntent(OtpVerification.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = BannerActivity.newIntent(OtpVerification.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void resendOTP() {
        mOtpVerificationViewModel.sendOtp(getIntent().getStringExtra(AppConstants.PHONE));
    }

    @Override
    public void backToEditNumber() {
        Intent intent = LoginActivity.newIntent(OtpVerification.this);
        startActivity(intent);
        finish();
    }
}
