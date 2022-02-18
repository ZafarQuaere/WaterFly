package com.waterfly.user.ui.userdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;
import com.waterfly.user.BR;
import com.waterfly.user.R;
import com.waterfly.user.ViewModelFactory;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.databinding.ActivityUserDetailsBinding;
import com.waterfly.user.ui.base.BaseActivity;
import com.waterfly.user.ui.main.MainActivity;
import com.waterfly.user.ui.otpverification.OtpVerification;
import com.waterfly.user.utils.AppValidationMassages;

public class UserDetailActivity extends BaseActivity<ActivityUserDetailsBinding, UserDetailViewModel> implements UserDetailNavigator {

    private ActivityUserDetailsBinding mActivityUserDetailsBinding;
    private UserDetailViewModel mUserDetailViewModel;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        return intent;
    }

    @Override
    public int getBindingVariable() {
        return BR.UserDetailViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_details;
    }

    @Override
    public UserDetailViewModel getViewModel() {
        ViewModelFactory factory = new ViewModelFactory(DataManager.getInstance());
        mUserDetailViewModel = ViewModelProviders.of(this, factory).get(UserDetailViewModel.class);
        return mUserDetailViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityUserDetailsBinding = getViewDataBinding();
        mUserDetailViewModel.setNavigator(this);
    }

    @Override
    public void handleError(Throwable throwable) {}

    @Override
    public void handleMassages(String massage) {
        mActivityUserDetailsBinding.txtErrorMassage.setText(massage);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void sendOtp() {
        String txtFullName = mActivityUserDetailsBinding.txtFullName.getText().toString();
        String txtEmail = mActivityUserDetailsBinding.txtEmailName.getText().toString();
        if (isNetworkConnected()) {
            if (mUserDetailViewModel.isUserDetailsValid(txtFullName,txtEmail)) {
                hideKeyboard();
                mUserDetailViewModel.saveUserDetails(txtFullName,txtEmail);
            }
        }else{
            mActivityUserDetailsBinding.txtErrorMassage.setText(AppValidationMassages.No_INTERNET);
        }
    }

    @Override
    public void openMainDashboardActivity() {
        Intent intent = MainActivity.newIntent(UserDetailActivity.this);
        startActivity(intent);
        finish();

    }
}