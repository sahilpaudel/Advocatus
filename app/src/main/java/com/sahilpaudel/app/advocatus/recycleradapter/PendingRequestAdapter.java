package com.sahilpaudel.app.advocatus.recycleradapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
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
import com.sahilpaudel.app.advocatus.dataprovider.PendingRequest;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;
import com.sahilpaudel.app.advocatus.fragments.PendingRequestFragment;
import com.squareup.picasso.Picasso;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sahil Paudel on 2/16/2017.
 */

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.MyViewHolder> {

    List<PendingRequest> list;
    Context context;

    public PendingRequestAdapter(Context context, List<PendingRequest> list) {
            this.context = context;
            this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_pic;
        TextView tv_description, tv_helperName, tv_helpTime, noofhelpers;
        Button buttonAccept, buttonReject;
        String helper_id;

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

    @Override
    public PendingRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewItem = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.pending_request, parent, false);
        return new PendingRequestAdapter.MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final PendingRequestAdapter.MyViewHolder holder, int position) {

        PendingRequest request = list.get(position);
        String first_name = request.firstName;
        String last_name = request.lastName;
        holder.tv_helperName.setText(first_name+" "+last_name);
        holder.tv_helpTime.setText(request.startTime+" to "+request.endTime);
        String imageUrl = "https://graph.facebook.com/" + request.facebook_id + "/picture?type=large";

        Picasso.with(context).load(imageUrl).into(holder.profile_pic);
        holder.noofhelpers.setText(request.no_of_helpers);
        holder.tv_description.setText(request.description);
        holder.helper_id = request.helper_id;
        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accept(holder.helper_id);
                holder.buttonAccept.setClickable(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    private void Accept(final String helper) {

        StringRequest request = new StringRequest(Request.Method.POST, Config.ACCEPT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")) {
                    Toast.makeText(context, "Thanks!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Ow snap! something went of the grid.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("helper_id",helper);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void Reject() {

    }
}
