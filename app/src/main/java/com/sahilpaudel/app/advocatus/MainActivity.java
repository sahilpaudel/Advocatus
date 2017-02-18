package com.sahilpaudel.app.advocatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.onesignal.OneSignal;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //facebook login button
    LoginButton FBbutton;

    //Facebook classes
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;

    //List to store userid and name of friends of loggedIn user
    List<String> userId;
    List<String> userName;

    //variables to store data returned from facebook
    String email;
    String firstName;
    String lastName;
    String facebook_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing facebook sdk
        FacebookSdk.sdkInitialize(this);

        //initializing one signal sdk

        OneSignal.startInit(this).init();
        setContentView(R.layout.activity_main);

        //facebook button reference
        FBbutton = (LoginButton) findViewById(R.id.FBLoginButton);

        //to check whether users previous session is still alive
        //if no accessToken will be null
        if(isLogin()) {
            Toast.makeText(this, "Is Logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        //sending the list of required information needed from the user loggedIn using facebook
        FBbutton.setReadPermissions(Arrays.asList("public_profile, email, user_friends,read_custom_friendlists"));

        //Initialize the callback manager
        callbackManager = CallbackManager.Factory.create();

        //register callbackManager to send login request to facebook
        FBbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //this method stores the data returned from facebook in json object
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                //reading the data from json object
                                try {

                                    facebook_id = object.getString("id");
                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    //String gender = object.getString("gender");
                                    email = object.getString("email");

                                    userId = new ArrayList<>();
                                    userName = new ArrayList<>();
                                    JSONObject obj = object.getJSONObject("friends");
                                    JSONArray array = obj.getJSONArray("data");

                                    //traversing the multilevel json array
                                    for(int i = 0; i < array.length(); i++) {
                                        userId.add(array.getJSONObject(i).getString("id"));
                                        userName.add(array.getJSONObject(i).getString("name"));
                                    }

                                    //storing the users info in the shared preferances for future references
                                    SharedPrefFacebook.getmInstance(MainActivity.this).saveUserInfo(firstName, lastName, email, facebook_id);
                                    SharedPrefFacebook.getmInstance(MainActivity.this).saveFacebookData(userId.toString(),userName.toString());

                                    //after fetching the data, check whether user exist or not
                                    if(firstName !=null && lastName != null && email != null)
                                        isExist(email);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                //to send request to facebook
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, first_name, last_name, friends");
                request.setParameters(parameters);
                request.executeAsync();

                //Access Token To Manage Logout
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if(currentAccessToken == null) {
                            SharedPrefFacebook.getmInstance(MainActivity.this).deleteFB();
                        }
                    }
                };
            }

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        email = SharedPrefFacebook.getmInstance(this).getUserInfo().get(2);
//        firstName = SharedPrefFacebook.getmInstance(this).getUserInfo().get(0);
//        lastName = SharedPrefFacebook.getmInstance(this).getUserInfo().get(1);


    }

    //method to check whether user already exist, checking it using facebook email
    //volley is used to send the email to server page
    //from where it checks in the database
    public void isExist(String email) {

        final String userEmail = email;

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_ISEXIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")) {
                    Toast.makeText(MainActivity.this, "isExist : "+response.equals("1"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{

                    // if user doesn't exist create new user
                    createUserOnServer(firstName, lastName, userEmail);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Volley Error : isExist"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",userEmail);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }


    public void createUserOnServer(String firstName, String lastName, String email) {

        //this sets of data is send to the server using volley
        final String userEmail = email;
        final String fName = firstName;
        final String lName = lastName;
        final String fb_id = SharedPrefFacebook.getmInstance(MainActivity.this).getUserInfo().get(3);

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_CREATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("1")) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "User Already Exist : "+response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Volley Error : create new"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name",fName);
                params.put("last_name",lName);
                params.put("email",userEmail);
                params.put("facebook_id",fb_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
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
