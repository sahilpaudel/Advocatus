package com.sahilpaudel.app.advocatus.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.sahilpaudel.app.advocatus.facebook.SimpleDividerItemDecoration;
import com.sahilpaudel.app.advocatus.recycleradapter.HelperListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SinglePostViewFragment extends Fragment {

    ImageView imageView;
    TextView textViewName;
    TextView textViewTime;
    TextView textViewHelpers;
    TextView textViewPost;

    Button buttonHelp;
    Button buttonPass;

    String facebook_id;
    String post_id;

    ProgressDialog progressDialog;

    RecyclerView mRecyclerView;
    HelperListAdapter mHelperListAdapter;

    List<HelperPerPost> mHelperList;

    public SinglePostViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_post_view, container, false);

        progressDialog = ProgressDialog.show(getActivity(),"Please wait.", "Patience is the key.", false, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewHelpers);

        String value = getArguments().getString("DATA");
        String[] data = value.split("<>");
        imageView = (ImageView)view.findViewById(R.id.poster_pic_1);
        textViewName = (TextView)view.findViewById(R.id.tv_posterName_1);
        textViewPost = (TextView)view.findViewById(R.id.userPost_1);
        textViewHelpers = (TextView)view.findViewById(R.id.nohelpers_1);
        textViewTime = (TextView)view.findViewById(R.id.timeTable_1);
        buttonHelp = (Button)view.findViewById(R.id.buttonhelp_1);
        buttonPass = (Button)view.findViewById(R.id.buttonPass_1);
        textViewName.setText(data[0]+" "+data[1]);
        textViewPost.setText(data[2]);
        textViewTime.setText(data[3]+" to "+data[4]);
        textViewHelpers.setText(data[5]);
        post_id = data[7];

        buttonHelp.setBackgroundColor(Color.parseColor("#b71c1c"));
        String url = "https://graph.facebook.com/" + data[6] + "/picture?type=large";
        Picasso.with(getActivity()).load(url).into(imageView);

        //facebook id of user of device
        facebook_id = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(3);
        //if the post facebook_id is same as loggedIn facebook_id then disable the help / pass button
        if (facebook_id.equals(data[6])) {

            buttonHelp.setVisibility(View.GONE);
            buttonPass.setVisibility(View.GONE);
        }


        //if already help button is clicked
        oldHelper();
        fetchHelper();
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newHelper();
            }
        });

        return view;
    }

    //to check whether he/she is already a helper
    private void oldHelper() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, Config.CHECK_HELPER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    progressDialog.dismiss();

                    if (response.equals("1")) {
                        buttonHelp.setClickable(true);
                        buttonHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners_second));


                    }else{
                        buttonHelp.setClickable(false);
                        buttonHelp.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_color_on_click));
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("post_id", post_id);
                params.put("facebook_id", facebook_id);
                return params;
            }

        };
        queue.add(request);
    }

    // to create a new helper if he/she is not already a helper of a post
    private void newHelper() {

        StringRequest request = new StringRequest(Request.Method.POST, Config.CREATE_HELPER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //when help button is visible and clicked
                if (response.equals("1")) {
                    Toast.makeText(getActivity(), "Thanks!", Toast.LENGTH_SHORT).show();
                    buttonHelp.setClickable(false);

                }else{
                    Toast.makeText(getActivity(), "Sorry, you Already did that!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("post_id", post_id);
                params.put("facebook_id", facebook_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    // fetch the details of helpers of a single post
    private void fetchHelper() {

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_GETHELPER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("0")) {
                    //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    try {
                        mHelperList = new ArrayList<>();
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            String fb_id = object.getString("facebook_id");
                            //Toast.makeText(getActivity(), firstName+" "+lastName+" "+fb_id, Toast.LENGTH_SHORT).show();
                            HelperPerPost help = new HelperPerPost();
                            help.firstName = firstName;
                            help.lastName = lastName;
                            help.facebook_id = fb_id;
                            mHelperList.add(help);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mHelperListAdapter = new HelperListAdapter(getActivity(),mHelperList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                    mRecyclerView.setAdapter(mHelperListAdapter);
                    mHelperListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "No Helper Found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("post_id", post_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

}
