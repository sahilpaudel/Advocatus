package com.sahilpaudel.app.advocatus.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sahil Paudel on 2/12/2017.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        storeToken(refreshedToken);
    }

    private void storeToken(String token){
        //to store in the sharedPreferences
        SharedPrefManager.getmInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
