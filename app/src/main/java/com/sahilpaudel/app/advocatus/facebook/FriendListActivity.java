package com.sahilpaudel.app.advocatus.facebook;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sahilpaudel.app.advocatus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Friends> mList_friends = new ArrayList<>();
    RecyclerViewAdapter mRecyclerViewAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        context = getApplicationContext();
        String userName = getIntent().getStringExtra("FRIEND_NAME");
        String userId = getIntent().getStringExtra("FRIEND_ID");

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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerViewAdapter = new RecyclerViewAdapter(context,mList_friends);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setBackgroundColor(Color.parseColor("#004d40"));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {

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

    }
}
