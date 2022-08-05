package com.waterfly.user.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.waterfly.user.R;

public class HelpNAboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_help_n_about);
        initUI();
    }

    private void initUI() {
        TextView textWebsite = findViewById(R.id.textWebsite);
        TextView text_fb_link = findViewById(R.id.text_fb_link);
        TextView text_insta_link = findViewById(R.id.text_insta_link);
        TextView text_twitter_link = findViewById(R.id.text_twitter_link);
        TextView textPlaystoreLink = findViewById(R.id.textPlaystoreLink);
        textWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        text_fb_link.setMovementMethod(LinkMovementMethod.getInstance());
        text_insta_link.setMovementMethod(LinkMovementMethod.getInstance());
        text_twitter_link.setMovementMethod(LinkMovementMethod.getInstance());
        textPlaystoreLink.setOnClickListener(v -> Util.openInPlayStore(this));
        findViewById(R.id.imgBtnBack).setOnClickListener(v -> onBackPressed());
    }
}