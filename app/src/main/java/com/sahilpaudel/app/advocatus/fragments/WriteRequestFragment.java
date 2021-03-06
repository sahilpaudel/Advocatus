package com.sahilpaudel.app.advocatus.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sahilpaudel.app.advocatus.Config;
import com.sahilpaudel.app.advocatus.HomeActivity;
import com.sahilpaudel.app.advocatus.R;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * a simple {@link Fragment} subclass.
 */
public class WriteRequestFragment extends Fragment {

    EditText mPost;
    EditText mStartTime;
    EditText mEndTime;
    Button mSubmit;
    String facebook_id;
    MaterialSpinner mCategory;

    String category;

    private int mYear, mDay, mMonth;

    public WriteRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_request, container, false);

        mCategory = (MaterialSpinner)view.findViewById(R.id.requestCategory);
        mPost = (EditText)view.findViewById(R.id.writepost);
        mStartTime = (EditText)view.findViewById(R.id.startTime);
        mEndTime = (EditText)view.findViewById(R.id.endTime);
        mSubmit = (Button)view.findViewById(R.id.buttonSubmit);
        facebook_id = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(3);

        mSubmit.setClickable(false);
        mPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               if (mPost.getText().length() > 30) {
                   mSubmit.setClickable(true);
               }
            }
        });

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current date
                final Calendar c = Calendar.getInstance();
                 mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mStartTime.setText(year+"/"+"0"+(month+1)+"/"+day);
                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mEndTime.setText(year+"/"+"0"+(month+1)+"/"+day);
                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        mCategory.setItems("Choose category","Movies", "Trip", "Households", "Personal Grooming","Local Visit","Emergency");
        mCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                category = item;
            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest request = new StringRequest(Request.Method.POST, Config.URL_CREATE_POST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")) {
                            Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                            sendNotification(category);
                            //Toast.makeText(getActivity(), "Notified", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new MyRequestFragment();
                            if(fragment != null) {
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.contentFragment, fragment);
                                transaction.commit();
                            }

                        }
                        else
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("facebook_id", facebook_id);
                        params.put("desc", mPost.getText().toString());
                        params.put("startTime", mStartTime.getText().toString());
                        params.put("endTime", mEndTime.getText().toString());
                        params.put("category", category);

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(request);
            }
        });


        return view;
    }

    public void sendNotification(final String category) {

        final String str1 = SharedPrefFacebook.getmInstance(getActivity()).getUserInfo().get(0);
        final String str2 = category;
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_GET_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")) {
                    Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new MyRequestFragment();
                    if(fragment != null) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.contentFragment, fragment);
                        transaction.commit();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", str1);
                params.put("category", str2);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
