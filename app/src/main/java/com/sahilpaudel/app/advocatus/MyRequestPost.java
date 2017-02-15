package com.sahilpaudel.app.advocatus;

/**
 * Created by Sahil Paudel on 2/13/2017.
 */

public class MyRequestPost {
    public String firstName;
    public String lastName;
    public String description;
    public String startTime;
    public String endTime;
    public String no_of_helpers;
    public String facebook_id;

    @Override
    public String toString() {
        return firstName+"<>"+lastName+"<>"+description+"<>"+startTime+"<>"+endTime+"<>"+no_of_helpers+"<>"+facebook_id;
    }
}
