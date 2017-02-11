package com.sahilpaudel.app.advocatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sahilpaudel.app.advocatus.facebook.FriendListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LoginButton FBbutton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    List<String> userId;
    List<String> userName;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);


        final TextView textView = (TextView) findViewById(R.id.textView);
        FBbutton = (LoginButton) findViewById(R.id.FBLoginButton);

        FBbutton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends,read_custom_friendlists"));
        callbackManager = CallbackManager.Factory.create();

        FBbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            String gender = object.getString("gender");
                            String birthday = object.getString("birthday");
                            String email = object.getString("email");

                            userId = new ArrayList<String>();
                            userName = new ArrayList<String>();
                            JSONObject obj = object.getJSONObject("friends");
                            JSONArray array = obj.getJSONArray("data");
                            //textView.setText(array.length());
                            for(int i = 0; i < array.length(); i++) {
                                userId.add(array.getJSONObject(i).getString("id"));
                                userName.add(array.getJSONObject(i).getString("name"));
                            }

                            Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                            intent.putExtra("FRIEND_ID",userId.toString());
                            intent.putExtra("FRIEND_NAME",userName.toString());
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday, first_name, last_name, friends");
        request.setParameters(parameters);
        request.executeAsync();

                //Access Token To Manage Logout
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if(currentAccessToken == null) {
                            //handle logout
                        }
                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
