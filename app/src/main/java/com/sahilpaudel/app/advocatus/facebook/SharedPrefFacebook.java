package com.sahilpaudel.app.advocatus.facebook;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Sahil Paudel on 2/12/2017.
 */

public class SharedPrefFacebook {
    private static final String FACEBOOK_PREFS = "FBPrefs" ;

    private static SharedPrefFacebook mInstance;
    private static Context mCtx;

    private SharedPrefFacebook(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefFacebook getmInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SharedPrefFacebook(context);
        }
        return mInstance;
    }

    public boolean saveUserInfo(String firstName, String lastName, String email, String id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FACEBOOK_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("fb_id",id);
        editor.apply();
        return true;
    }

    public boolean saveFacebookData(String userId, String userName) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FACEBOOK_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FRIEND_ID", userId.toString());
        editor.putString("FRIEND_NAME", userName.toString());
        editor.apply();
        return true;
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FACEBOOK_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString("FRIEND_ID", null);
    }
    public String getUserName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FACEBOOK_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString("FRIEND_NAME", null);
    }

    public ArrayList<String> getUserInfo () {
        ArrayList<String> data = new ArrayList<>();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FACEBOOK_PREFS, Context.MODE_PRIVATE);
        data.add(sharedPreferences.getString("firstName",""));
        data.add(sharedPreferences.getString("lastName", ""));
        data.add(sharedPreferences.getString("email", ""));
        data.add(sharedPreferences.getString("fb_id", ""));

        return data;
    }

    public void deleteFB() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(FACEBOOK_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear();
    }

}
