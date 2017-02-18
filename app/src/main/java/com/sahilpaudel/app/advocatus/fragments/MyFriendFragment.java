package com.sahilpaudel.app.advocatus.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.facebook.ClickListener;
import com.sahilpaudel.app.advocatus.facebook.Friends;
import com.sahilpaudel.app.advocatus.facebook.RecyclerTouchListener;
import com.sahilpaudel.app.advocatus.facebook.RecyclerViewAdapter;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.sahilpaudel.app.advocatus.facebook.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * a simple {@link Fragment} subclass.
 */
public class MyFriendFragment extends Fragment {

    RecyclerView recyclerView;
    List<Friends> mList_friends = new ArrayList<>();
    RecyclerViewAdapter mRecyclerViewAdapter;
    Context context;

    public MyFriendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_friend, container, false);


        String userName = SharedPrefFacebook.getmInstance(getActivity()).getUserName();
        String userId = SharedPrefFacebook.getmInstance(getActivity()).getUserId();


        String s = userName.replaceAll("\\[","");
        s = s.replaceAll("\\]","");
        String[] nameList = s.split(",");

        String s1 = userId.replaceAll("\\[","");
        s1 = s1.replaceAll("\\]","");
        String[] idList = s1.replaceAll(" ","").split(",");

        List<String> name = Arrays.asList(nameList);
        List<String> id = Arrays.asList(idList);

        final int len = name.size();

        for(int i = 0; i < id.size(); i++) {
            Friends mFriends = new Friends();
            mFriends.setFriendID(id.get(i).toString());
            mFriends.setFriendName(name.get(i).toString());
            mFriends.setImageURL("https://graph.facebook.com/" + id.get(i).toString() + "/picture?type=large");

            mList_friends.add(mFriends);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(),mList_friends);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {

            ArrayList selected = new ArrayList<>(Collections.nCopies(len,""));
            ArrayList<Integer> flag = new ArrayList<>(Collections.nCopies(len,0));

            @Override
            public void onClick(View view, int position) {
                Friends friends = mList_friends.get(position);

                if(flag.get(position) == 0){
                    selected.add(position,friends);
                    flag.add(position,1);
                    view.setSelected(true);
                }else{
                    flag.set(position,0);
                    selected.remove(position);
                    view.setSelected(false);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }

}
