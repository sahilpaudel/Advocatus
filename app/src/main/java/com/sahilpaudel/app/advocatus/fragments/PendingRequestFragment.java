package com.sahilpaudel.app.advocatus.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sahilpaudel.app.advocatus.dataprovider.PendingRequest;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.sahilpaudel.app.advocatus.facebook.SimpleDividerItemDecoration;
import com.sahilpaudel.app.advocatus.recycleradapter.PendingRequestAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a simple {@link Fragment} subclass.
 */
public class PendingRequestFragment extends Fragment {


    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    PendingRequestAdapter pendingRequestAdapter;
    List<PendingRequest> myPendingRequest;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String facebook_id;

    StringRequest request;

    public PendingRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_request, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.pendingRecycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        myPendingRequest = new ArrayList<>();

        facebook_id = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(3);

        progressDialog = ProgressDialog.show(getActivity(),"Please wait.","Notifications are buzzing", false, false);
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        request = new StringRequest(Request.Method.POST, Config.CONF_HELP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");

                    if(success.equals("true")) {

                        JSONArray array = object.getJSONArray("data");

                        for(int i = 0; i < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);

                            String post_id = data.getString("post_id");
                            String description = data.getString("description");
                            String startTime = data.getString("startTime");
                            String endTime = data.getString("endTime");
                            String no_of_helpers = data.getString("no_of_helpers");
                            String fb = data.getString("facebook_id");
                            String firstName = data.getString("first_name");
                            String lastName = data.getString("last_name");
                            String helper_id = data.getString("helper_id");

                            PendingRequest request = new PendingRequest();
                            request.post_id = post_id;
                            request.description = description;
                            request.facebook_id = fb;
                            request.firstName = firstName;
                            request.lastName = lastName;
                            request.no_of_helpers = no_of_helpers;
                            request.startTime = startTime;
                            request.endTime = endTime;
                            request.helper_id = helper_id;

                            myPendingRequest.add(request);
                            progressDialog.dismiss();
                        }
                        pendingRequestAdapter = new PendingRequestAdapter(getActivity(), myPendingRequest);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                        recyclerView.setAdapter(pendingRequestAdapter);

                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                pendingRequestAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }else{
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
                params.put("facebook_id", facebook_id);
                return params;
            }
        };
        queue.add(request);

        return view;
    }


}
