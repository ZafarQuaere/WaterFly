package com.waterfly.user.ui.userdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.waterfly.user.R;
import com.waterfly.user.data.DataManager;
import com.waterfly.user.data.local.prefs.AppPreferencesHelper;

public class UserProfile extends AppCompatActivity {
    private AppPreferencesHelper prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        prefs = DataManager.getInstance().getSharedPreference();
        initUI();
    }

    private void initUI() {
        TextView textUserName = findViewById(R.id.textUserName);
        TextView textUserPhone = findViewById(R.id.textUserPhone);
        textUserName.setText(prefs.getUserName());
        textUserPhone.setText(prefs.getUserPhone());
        findViewById(R.id.imgBtnBack).setOnClickListener(v-> onBackPressed());
    }
}