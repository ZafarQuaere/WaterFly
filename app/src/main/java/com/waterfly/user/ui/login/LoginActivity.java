package com.waterfly.user.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;
import com.waterfly.user.BR;
import com.waterfly.user.R;
import com.waterfly.user.ViewModelFactory;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.databinding.ActivityLoginBinding;
import com.waterfly.user.ui.base.BaseActivity;
import com.waterfly.user.ui.otpverification.OtpVerification;
import com.waterfly.user.utils.AppConstants;
import com.waterfly.user.utils.AppValidationMassages;


public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator{

    private ActivityLoginBinding mActivityLoginBinding;
    private LoginViewModel mLoginViewModel;
    private String mMobileNumber;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.LoginViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        ViewModelFactory factory = new ViewModelFactory(DataManager.getInstance());
        mLoginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        return mLoginViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mLoginViewModel.setNavigator(this);
    }

    @Override
    public void handleError(Throwable throwable) {}

    @Override
    public void handleMassages(String massage) {
        mActivityLoginBinding.txtErrorMassage.setVisibility(View.VISIBLE);
        mActivityLoginBinding.txtErrorMassage.setText(massage);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void sendOtp() {
        String mobileNumber = mActivityLoginBinding.txtMobileNumber.getText().toString();
        if (isNetworkConnected()) {
            if (mLoginViewModel.isEmailAndPasswordValid(mobileNumber)) {
                mActivityLoginBinding.txtErrorMassage.setVisibility(View.GONE);
                mMobileNumber = mobileNumber;
                hideKeyboard();
                mLoginViewModel.sendOtp(mobileNumber);
            }
        }else{
            mActivityLoginBinding.txtErrorMassage.setVisibility(View.VISIBLE);
//            mActivityLoginBinding.txtErrorMassage.setText(!mActivityLoginBinding.termsCheckBox.isChecked() ? AppValidationMassages.CHECK_TERMS :AppValidationMassages.No_INTERNET);
            mActivityLoginBinding.txtErrorMassage.setText(AppValidationMassages.No_INTERNET);
        }
    }

    @Override
    public void openOtpVerificationActivity() {
        Intent intent = OtpVerification.newIntent(LoginActivity.this);
        intent.putExtra(AppConstants.PHONE, mMobileNumber);
        startActivity(intent);
        finish();
    }
}