package com.sahilpaudel.app.advocatus.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sahilpaudel.app.advocatus.Config;
import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.dataprovider.HelperPerPost;
import com.sahilpaudel.app.advocatus.dataprovider.User;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    ImageView mImageView;
    TextView mTotalPost, mPoints, mTotalHelp,mUserName;
    List<User> list;
    String facebook_id = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(3);
    ProgressDialog progressDialog;
    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
            mImageView = (ImageView)view.findViewById(R.id.iv_profilePic);
            mTotalPost = (TextView)view.findViewById(R.id.tv_totalpost);
            mTotalHelp = (TextView)view.findViewById(R.id.tv_totalhelp);
            mPoints = (TextView)view.findViewById(R.id.tv_points);
            mUserName = (TextView)view.findViewById(R.id.tv_userName);

        progressDialog = ProgressDialog.show(getActivity(),"Please wait.","Your Profile in being furnished.", true, false);
        getUserInfo();

        return view;
    }

    private void getUserInfo() {

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_GET_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("0")) {
                    progressDialog.dismiss();
                    try {
                        list = new ArrayList<>();
                        JSONArray array = new JSONArray(response);


                            JSONObject object = array.getJSONObject(0);
                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            String fb_id = object.getString("facebook_id");
                            String points = object.getString("points");
                            String nohelps = object.getString("no_of_helps");
                            JSONObject object1 = array.getJSONObject(1);
                            String totalpost = object1.getString("total_post");
                            //Toast.makeText(getActivity(), array.length(), Toast.LENGTH_SHORT).show();
                            mUserName.setText(firstName+" "+lastName);
                            Picasso.with(getActivity()).load("https://graph.facebook.com/" + fb_id + "/picture?type=large").into(mImageView);
                            mTotalHelp.setText(nohelps+"\nTotal helps");
                            mPoints.setText(points+"\nTotal Points");
                            mTotalPost.setText(totalpost+"\nTotal Request");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("facebook_id", facebook_id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

}
