package com.sahilpaudel.app.advocatus.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sahilpaudel.app.advocatus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteRequestFragment extends Fragment {

    public WriteRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_request, container, false);

        MaterialSpinner mCategory = (MaterialSpinner)view.findViewById(R.id.requestCategory);
        EditText mPost = (EditText)view.findViewById(R.id.writepost);
        EditText mStartTime = (EditText)view.findViewById(R.id.startTime);
        EditText mEndTime = (EditText)view.findViewById(R.id.endTime);
        Button mSubmit = (Button)view.findViewById(R.id.buttonSubmit);
        return view;
    }

}
