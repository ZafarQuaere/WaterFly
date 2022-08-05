package com.waterfly.user.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.waterfly.user.BuildConfig;
import com.waterfly.user.R;

public class Util {

    public static void shareApp(Activity activity) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WaterFly");
            String shareMessage = activity.getString(R.string.share_app_content);
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.select_app_to_share)));
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.ERROR(e.getMessage());
        }
    }

    public static void openInPlayStore(Activity activity) {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            Uri uri = Uri.parse("market://details?id=" + appPackageName);
            activity.startActivity(new Intent(Intent.ACTION_VIEW,uri));
        } catch (android.content.ActivityNotFoundException anfe) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}
