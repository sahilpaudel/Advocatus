package com.sahilpaudel.app.advocatus.recycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.dataprovider.PendingRequest;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sahil Paudel on 2/16/2017.
 */

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.MyViewHolder> {

    List<PendingRequest> list;
    Context context;

    ImageView profile_pic;
    TextView tv_description, tv_helperName, tv_helpTime, noofhelpers;
    Button buttonAccept, buttonReject;

    public PendingRequestAdapter(Context context, List<PendingRequest> list) {
            this.context = context;
            this.list = list;
    }

    @Override
    public PendingRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewItem = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.pending_request, parent, false);
        return new PendingRequestAdapter.MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(PendingRequestAdapter.MyViewHolder holder, int position) {

        PendingRequest request = list.get(position);
        String first_name = request.firstName;
        String last_name = request.lastName;
        tv_helperName.setText(first_name+" "+last_name);
        tv_helpTime.setText(request.startTime+" to "+request.endTime);
        String imageUrl = "https://graph.facebook.com/" + request.facebook_id + "/picture?type=large";

        Picasso.with(context).load(imageUrl).into(profile_pic);
        noofhelpers.setText(request.no_of_helpers);
        tv_description.setText(request.description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);
            profile_pic = (ImageView)itemView.findViewById(R.id.helperImage);
            tv_helperName = (TextView)itemView.findViewById(R.id.helperName);
            tv_description = (TextView)itemView.findViewById(R.id.postDesc);
            tv_helpTime = (TextView)itemView.findViewById(R.id.timeLimit);
            noofhelpers = (TextView)itemView.findViewById(R.id.no_of_helpers);
            buttonAccept = (Button)itemView.findViewById(R.id.buttonAccept);
            buttonReject = (Button)itemView.findViewById(R.id.buttonReject);

        }
    }
}
