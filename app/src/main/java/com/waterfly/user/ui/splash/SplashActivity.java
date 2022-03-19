package com.waterfly.user.ui.splash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.waterfly.user.BR;
import com.waterfly.user.R;
import com.waterfly.user.ViewModelFactory;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.databinding.ActivitySplashBinding;
import com.waterfly.user.ui.base.BaseActivity;
import com.waterfly.user.ui.login.LoginActivity;
import com.waterfly.user.ui.main.BannerActivity;


public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {


    private SplashViewModel mSplashViewModel;
    private boolean gpsStatus;
    private AlertDialog alertDialog = null;
    public static final int GPS_STATUS_REQUEST = 10;

    @Override
    public int getBindingVariable() {
        return BR.LoginViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashViewModel.setNavigator(this);
        mSplashViewModel.startSeeding();
        createGPSDialog();
    }

    public void CheckGpsStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            alertDialog.dismiss();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = BannerActivity.newIntent(SplashActivity.this);
                    startActivity(intent);
                    finish();
                }
            },500);
        } else {
            openGPSDialog();
        }
    }

    private void createGPSDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.gps_dialog_title))
                .setMessage(getString(R.string.gps_dialog_message))
                .setPositiveButton(getString(R.string.gps_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_STATUS_REQUEST);
                    }
                }).setCancelable(false);
        alertDialog = alert.create();
    }

    private void openGPSDialog() {
        alertDialog.show();
    }

    @Override
    public SplashViewModel getViewModel() {
        ViewModelFactory factory = new ViewModelFactory(DataManager.getInstance());
        mSplashViewModel = ViewModelProviders.of(this,factory).get(SplashViewModel.class);
        return mSplashViewModel;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        CheckGpsStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GPS_STATUS_REQUEST) {
            CheckGpsStatus();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
