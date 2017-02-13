package com.sahilpaudel.app.advocatus.notification;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.RequestParams;
import com.sahilpaudel.app.advocatus.R;

public class NotificationActivity extends AppCompatActivity {

    Button mButttonShowToken;
    TextView mTextViewShowToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mButttonShowToken = (Button) findViewById(R.id.loadTokenButton);
        mTextViewShowToken = (TextView) findViewById(R.id.tokenDisplayView);

        mButttonShowToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void sendTokenToServer() {

        final String token = SharedPrefManager.getmInstance(this).getDeviceToken();
        //final String fbId =
    }

}
