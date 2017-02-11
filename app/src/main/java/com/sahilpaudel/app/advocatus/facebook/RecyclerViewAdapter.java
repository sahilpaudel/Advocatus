package com.sahilpaudel.app.advocatus.facebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahilpaudel.app.advocatus.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sahil Paudel on 2/10/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    List<Friends> list;
    Context context;

    public TextView tv_userName, tv_userId;
    public ImageView iv_profile;

    public RecyclerViewAdapter(Context context, List<Friends> list) {
        this.list = list;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);
            tv_userId = (TextView)view.findViewById(R.id.friend_id);
            tv_userName = (TextView)view.findViewById(R.id.friend_name);
            iv_profile = (ImageView)view.findViewById(R.id.friend_pic);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
           Friends friends = list.get(position);
           tv_userId.setText(friends.getFriendID());
           tv_userName.setText(friends.getFriendName());
           Picasso.with(context).load(friends.getImageURL()).into(iv_profile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
