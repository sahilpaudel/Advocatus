package com.sahilpaudel.app.advocatus.notification;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * Created by Sahil Paudel on 2/12/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0) {
            try{
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
            }catch (Exception e){

            }
        }
    }
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        try{
            JSONObject data = json.getJSONObject("data");

            //parse the data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //create MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //create intent for the notification
            Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);

            //if no image
            if(imageUrl.equals("null")) {
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }

        }catch (Exception e){

        }
    }
}