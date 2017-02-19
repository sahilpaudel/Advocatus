package com.sahilpaudel.app.advocatus.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * a simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    UserFeedAdapter userFeedAdapter;
    List<Feeds> mFeedList;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressDialog mProgressDialog;
    StringRequest request;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.myFeedRecyclerView);

        //reference for swipe refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        mProgressDialog = ProgressDialog.show(getActivity(),"Please wait.","Feeds are being loaded",false);

        //to store the feed data from the server
        mFeedList = new ArrayList<>();

        //start of volley request from where the data will be fetched
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        request = new StringRequest(Request.Method.POST, Config.URL_GETPOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");

                    //if success returns true
                    if(success.equals("true")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i  < array.length(); i++) {

                            JSONObject data = array.getJSONObject(i);

                            String firstName = data.getString("first_name");
                            String lastName = data.getString("last_name");
                            String post_id = data.getString("post_id");
                            String description = data.getString("description");
                            String tstartTime = data.getString("startTime");
                            String tendTime = data.getString("endTime");
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date dstartTime =  df.parse(tstartTime);
                            Date dendTime = df.parse(tendTime);
                            DateFormat dtf = new SimpleDateFormat("EEE, MMM d",Locale.ENGLISH);
                            String startTime = dtf.format(dstartTime);
                            String endTime = dtf.format(dendTime);



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

                            //dismiss the dialog where we get the response
                            mProgressDialog.dismiss();
                        }

                        //recycler view adapter
                        userFeedAdapter = new UserFeedAdapter(getActivity(),mFeedList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                        recyclerView.setAdapter(userFeedAdapter);

                        //Listener is attached on the swipe to refresh layout
                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                //a new list is created to avoid the
                                // addition of old data again on the list

                                //create new list
                                List<Feeds> feeds = new ArrayList<Feeds>();
                                //clear old list
                                mFeedList.clear();
                                //make the feed blank;
                                userFeedAdapter.notifyDataSetChanged();

                                //make new request
                                queue.add(request);
                                //fill the layout with views
                                userFeedAdapter = new UserFeedAdapter(getActivity(), mFeedList);
                                recyclerView.setAdapter(userFeedAdapter);

                                //refill the list with updated data
                                feeds.addAll(mFeedList);
                                //populate the recycler view
                                userFeedAdapter.notifyDataSetChanged();

                            }
                        });

                        // Configure the refreshing colors
                        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                                android.R.color.holo_green_light,
                                android.R.color.holo_orange_light,
                                android.R.color.holo_red_light);

                        //recyclerview listener
                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                            @Override
                            public void onClick(View view, int position) {

                                //fragment to display single feed in one page
                                Fragment fragment = new SinglePostViewFragment();
                                Feeds feeds = mFeedList.get(position);
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

        queue.add(request);

        return view;
    }

}
