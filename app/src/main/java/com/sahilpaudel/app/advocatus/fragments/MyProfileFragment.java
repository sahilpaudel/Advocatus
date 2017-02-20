package com.sahilpaudel.app.advocatus.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sahilpaudel.app.advocatus.dataprovider.MyRequestPost;
import com.sahilpaudel.app.advocatus.dataprovider.User;
import com.sahilpaudel.app.advocatus.facebook.ClickListener;
import com.sahilpaudel.app.advocatus.facebook.RecyclerTouchListener;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.sahilpaudel.app.advocatus.facebook.SimpleDividerItemDecoration;
import com.sahilpaudel.app.advocatus.recycleradapter.MyRequestViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sahilpaudel.app.advocatus.R.id.recyclerView;

/**
 * a simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    ImageView mImageView;
    TextView mTotalPost, mPoints, mTotalHelp,mUserName;
    List<MyRequestPost> userPost;

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    MyRequestViewAdapter myRequestViewAdapter;

   // String firstName;
    //String lastName;
    String facebook_id = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(3);

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
            recyclerView = (RecyclerView) view.findViewById(R.id.userPofileRecyclerView);
            progressDialog = ProgressDialog.show(getActivity(),"Please wait.","Your Profile in being furnished.", true, false);

        //getting the facebook_id from onclick listener on the name
        //in the home fragment
        try {
            String temp;
            temp = getArguments().get("FB_ID").toString();
            Toast.makeText(getActivity(), temp, Toast.LENGTH_SHORT).show();
            if (temp != null) {
                facebook_id = temp;
            }
        }catch (NullPointerException ignored){

        }
        //getUserInfo();
        userContribution(facebook_id);

        return view;
    }

    private void getUserInfo() {

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_GET_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("0")) {
                    progressDialog.dismiss();
                    try {

                        JSONArray array = new JSONArray(response);


                            JSONObject object = array.getJSONObject(0);
                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            facebook_id = object.getString("facebook_id");
                            String points = object.getString("points");
                            String nohelps = object.getString("no_of_helps");
                            JSONObject object1 = array.getJSONObject(1);
                            String totalpost = object1.getString("total_post");
                            //Toast.makeText(getActivity(), array.length(), Toast.LENGTH_SHORT).show();
                            mUserName.setText(firstName+" "+lastName);
                            Picasso.with(getActivity()).load("https://graph.facebook.com/" + facebook_id + "/picture?type=large").into(mImageView);
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


    private void userContribution(final String fbid) {

        getUserInfo();

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_GETPOST_BYID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    userPost = new ArrayList<>();
                    if(success.equals("true")) {
                        //Toast.makeText(getActivity(), "PASS", Toast.LENGTH_SHORT).show();
                        JSONArray array = object.getJSONArray("data");
                        //Toast.makeText(getActivity(), array.toString(), Toast.LENGTH_LONG).show();
                        for (int i = 0; i  < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);

                            String firstName = data.getString("first_name");
                            String lastName = data.getString("last_name");

                            //Toast.makeText(getActivity(), firstName+" "+lastName, Toast.LENGTH_SHORT).show();

                            String post_id = data.getString("post_id");
                            String description = data.getString("description");
                            String tstartTime = data.getString("startTime");
                            String tendTime = data.getString("endTime");
                            String no_of_helpers = data.getString("no_of_helpers");
                            String fb = data.getString("facebook_id");
                            //Toast.makeText(getActivity(), fb, Toast.LENGTH_SHORT).show();
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date dstartTime =  df.parse(tstartTime);
                            Date dendTime = df.parse(tendTime);
                            DateFormat dtf = new SimpleDateFormat("EEE, MMM d",Locale.ENGLISH);
                            String startTime = dtf.format(dstartTime);
                            String endTime = dtf.format(dendTime);


                            MyRequestPost myPost = new MyRequestPost();
                            myPost.firstName = firstName;
                            myPost.lastName = lastName;
                            myPost.description = description;
                            myPost.startTime = startTime;
                            myPost.endTime = endTime;
                            myPost.no_of_helpers = no_of_helpers;
                            myPost.facebook_id = fb;
                            myPost.post_id = post_id;

                            userPost.add(myPost);

                            progressDialog.dismiss();
                        }

                        myRequestViewAdapter = new MyRequestViewAdapter(getActivity(),userPost);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                        recyclerView.setAdapter(myRequestViewAdapter);

                        myRequestViewAdapter.notifyDataSetChanged();

                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Fragment fragment = new SinglePostViewFragment();
                                MyRequestPost feeds = userPost.get(position);
                                Bundle args = new Bundle();
                                args.putString("DATA",feeds.toString());
                                fragment.setArguments(args);

                                if(fragment != null) {
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.contentFragment, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));
                    }else {
                        Toast.makeText(getActivity(), "FAILED", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }catch (Exception e){
                    progressDialog.dismiss();;
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                    //e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("facebook_id",fbid);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);




    }

}
