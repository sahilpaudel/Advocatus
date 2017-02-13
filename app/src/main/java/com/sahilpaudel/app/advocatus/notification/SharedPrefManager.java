package com.sahilpaudel.app.advocatus.notification;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sahil Paudel on 2/12/2017.
 */

public class SharedPrefManager {
    private static final String FCM_SHARED_PREF = "FCMSharedPref";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getmInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FCM_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FCM_DEVICE_TOKEN",token);
        editor.apply();
        return true;
    }

    public String getDeviceToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FCM_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString("FCM_DEVICE_TOKEN", null);
    }
}
