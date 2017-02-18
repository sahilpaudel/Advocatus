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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sahilpaudel.app.advocatus.Config;
import com.sahilpaudel.app.advocatus.dataprovider.Feeds;
import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.recycleradapter.UserFeedAdapter;
import com.sahilpaudel.app.advocatus.facebook.ClickListener;
import com.sahilpaudel.app.advocatus.facebook.RecyclerTouchListener;
import com.sahilpaudel.app.advocatus.facebook.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * a simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    UserFeedAdapter userFeedAdapter;
    List<Feeds> mFeedList;

    ProgressDialog mProgressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.myFeedRecyclerView);

        mProgressDialog = ProgressDialog.show(getActivity(),"Please wait.","Feeds are being loaded",false);

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_GETPOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    mFeedList = new ArrayList<>();
                    if(success.equals("true")) {
                        //Toast.makeText(getActivity(), "PASS", Toast.LENGTH_SHORT).show();
                        JSONArray array = object.getJSONArray("data");
                        //Toast.makeText(getActivity(), array.toString(), Toast.LENGTH_LONG).show();
                        for (int i = 0; i  < array.length(); i++) {
                            JSONObject data = array.getJSONObject(i);

                            String firstName = data.getString("first_name");
                            String lastName = data.getString("last_name");
                            String post_id = data.getString("post_id");
                            String description = data.getString("description");
                            String startTime = data.getString("startTime");
                            String endTime = data.getString("endTime");
                            String no_of_helpers = data.getString("no_of_helpers");
                            String facebook_id = data.getString("facebook_id");


                            Feeds myPost = new Feeds();
                            myPost.firstName = firstName;
                            myPost.lastName = lastName;
                            myPost.description = description;
                            myPost.startTime = startTime;
                            myPost.endTime = endTime;
                            myPost.no_of_helpers = no_of_helpers;
                            myPost.facebook_id = facebook_id;
                            myPost.post_id = post_id;
                            mFeedList.add(myPost);
                            mProgressDialog.dismiss();
                        }

                        userFeedAdapter = new UserFeedAdapter(getActivity(),mFeedList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                        recyclerView.setAdapter(userFeedAdapter);

                        userFeedAdapter.notifyDataSetChanged();

                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Fragment fragment = new SinglePostViewFragment();
                                Feeds feeds = mFeedList.get(position);
                                //Toast.makeText(getActivity(), feeds.facebook_id, Toast.LENGTH_SHORT).show();
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
                        mProgressDialog.dismiss();
                    }

                }catch (Exception e){
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();

            }
        }){

        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


        return view;
    }

}
