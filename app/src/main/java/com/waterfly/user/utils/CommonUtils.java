package com.waterfly.user.utils;

import android.util.Patterns;


public final class CommonUtils {

    public static boolean isPhoneNumberValid(String number) {
        if(number.length() < 10){
            return false;
        }
        return true;
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidUserName(String str) {
        if(str.length() < 3 || str.length() > 30){
            return false;
        }
        return true;
    }

    public static boolean isValidOTP(String str) {
        if(str.length() < 6){
            return false;
        }
        return true;
    }

}
