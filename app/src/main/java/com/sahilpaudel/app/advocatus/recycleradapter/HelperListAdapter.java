package com.sahilpaudel.app.advocatus.recycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.dataprovider.HelperPerPost;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sahil Paudel on 2/18/2017.
 */

public class HelperListAdapter extends RecyclerView.Adapter<HelperListAdapter.MyViewHolder> {

    Context context;
    List<HelperPerPost> helperPerPostList;

    public HelperListAdapter(Context context, List<HelperPerPost> helperPerPostList) {
            this.context = context;
            this.helperPerPostList = helperPerPostList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.helperImage);
            mTextView = (TextView)itemView.findViewById(R.id.helperName);
        }
    }
    @Override
    public HelperListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.helperview,parent, false);
        return new HelperListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HelperListAdapter.MyViewHolder holder, int position) {
        HelperPerPost helperPerPost = helperPerPostList.get(position);
        Picasso.with(context)
                .load("https://graph.facebook.com/" + helperPerPost.facebook_id + "/picture?type=large")
                .into(holder.mImageView);
        holder.mTextView.setText(helperPerPost.firstName+" "+helperPerPost.lastName);
    }

    @Override
    public int getItemCount() {
        return helperPerPostList.size();
    }
}
