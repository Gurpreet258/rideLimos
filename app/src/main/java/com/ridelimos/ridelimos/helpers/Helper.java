package com.ridelimos.ridelimos.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.ridelimos.ridelimos.MyApplication;

/**
 * Created by zebarahman on 6/9/17.
 */

public class Helper {

    public static String getAuthorizationValue(String user, String pass) {
        final String userAndPassword = user + ":" + pass;
        byte[] encodedAuth= Base64.encode(userAndPassword.getBytes(),Base64.NO_WRAP);
        final String basic = "Basic " + new String(encodedAuth);
        return basic;
    }

    public static String getSavedToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String token = prefs.getString("token",null);
        return token;
    }

    public static Boolean tokenExists() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        if (prefs.contains("token")) {
            return true;
        } else return false;
    }

}
