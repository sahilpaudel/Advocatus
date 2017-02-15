package com.sahilpaudel.app.advocatus.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sahilpaudel.app.advocatus.Feeds;
import com.sahilpaudel.app.advocatus.R;
import com.squareup.picasso.Picasso;

public class SinglePostViewFragment extends Fragment {

    ImageView imageView;
    TextView textViewName;
    TextView textViewTime;
    TextView textViewHelpers;
    TextView textViewPost;

    Button buttonHelp;
    Button buttonPass;

    public SinglePostViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_post_view, container, false);

        String value = getArguments().getString("DATA");
        String[] data = value.split("<>");
        imageView = (ImageView)view.findViewById(R.id.poster_pic_1);
        textViewName = (TextView)view.findViewById(R.id.tv_posterName_1);
        textViewPost = (TextView)view.findViewById(R.id.userPost_1);
        textViewHelpers = (TextView)view.findViewById(R.id.nohelpers_1);
        textViewTime = (TextView)view.findViewById(R.id.timeTable_1);
        buttonHelp = (Button)view.findViewById(R.id.buttonhelp_1);
        buttonPass = (Button)view.findViewById(R.id.buttonPass_1);
        textViewName.setText(data[0]+" "+data[1]);
        textViewPost.setText(data[2]);
        textViewTime.setText(data[3]+" to "+data[4]);
        textViewHelpers.setText(data[5]);
        String url = "https://graph.facebook.com/" + data[6] + "/picture?type=large";
        Picasso.with(getActivity()).load(url).into(imageView);
        return view;
    }

}
