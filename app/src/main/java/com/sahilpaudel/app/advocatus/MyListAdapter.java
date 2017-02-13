package com.sahilpaudel.app.advocatus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Sahil Paudel on 2/13/2017.
 */

public class MyListAdapter extends BaseAdapter {

    Activity activity;
    LayoutInflater inflater;
    List<Feeds> list;

    public MyListAdapter(Activity activity, List<Feeds> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null) {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null) {
            view = inflater.inflate(R.layout.feed_view, null);
        }

        TextView userName = (TextView)view.findViewById(R.id.tv_username);
        TextView startTime = (TextView)view.findViewById(R.id.startTime);
        TextView endTime = (TextView)view.findViewById(R.id.endTime);
        TextView helpers = (TextView)view.findViewById(R.id.helpers);

        Feeds feeds = list.get(i);
        userName.setText(feeds.getmUserName());
        startTime.setText(feeds.getmStartTime());
        helpers.setText(feeds.getHelpers());
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date d = sdf.parse(feeds.getmStartTime());
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, Integer.parseInt(feeds.getmLimit()));
            String endDate = sdf.format(c.getTime());
            endTime.setText(endDate);

        }catch (Exception e){

        }

        return view;
    }
}
