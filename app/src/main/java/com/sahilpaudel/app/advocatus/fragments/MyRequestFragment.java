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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sahilpaudel.app.advocatus.dataprovider.MyRequestPost;
import com.sahilpaudel.app.advocatus.recycleradapter.MyRequestViewAdapter;
import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.facebook.ClickListener;
import com.sahilpaudel.app.advocatus.facebook.RecyclerTouchListener;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.sahilpaudel.app.advocatus.facebook.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRequestFragment extends Fragment {

    String facebook_id;
    List<MyRequestPost> myRequestPost;

    RecyclerView recyclerView;
    MyRequestViewAdapter myRequestViewAdapter;

    ProgressDialog mProgressDialog;

    public MyRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_request, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.myRequestRecyclerView);
        facebook_id = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(3);
        mProgressDialog = ProgressDialog.show(getActivity(),"Please wait.","Your request are being loaded");
        StringRequest request = new StringRequest(Request.Method.POST, "https://advocatus.azurewebsites.net/api/getPostById.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    myRequestPost = new ArrayList<>();
                    if(success.equals("true")) {
                        //Toast.makeText(getActivity(), "PASS", Toast.LENGTH_SHORT).show();
                        JSONArray array = object.getJSONArray("data");
                        //Toast.makeText(getActivity(), array.toString(), Toast.LENGTH_LONG).show();
                        for (int i = 0; i  < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);

                            String post_id = data.getString("post_id");
                            String description = data.getString("description");
                            String startTime = data.getString("startTime");
                            String endTime = data.getString("endTime");
                            String no_of_helpers = data.getString("no_of_helpers");
                            String fb = data.getString("facebook_id");



                            MyRequestPost myPost = new MyRequestPost();
                            myPost.firstName = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(0);
                            myPost.lastName = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(1);
                            myPost.description = description;
                            myPost.startTime = startTime;
                            myPost.endTime = endTime;
                            myPost.no_of_helpers = no_of_helpers;
                            myPost.facebook_id = fb;
                            myPost.post_id = post_id;

                            myRequestPost.add(myPost);

                            mProgressDialog.dismiss();
                        }

                        myRequestViewAdapter = new MyRequestViewAdapter(getActivity(),myRequestPost);
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
                                MyRequestPost feeds = myRequestPost.get(position);
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
                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(), "Exception : "+e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("facebook_id",facebook_id);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


        return view;
    }

}
