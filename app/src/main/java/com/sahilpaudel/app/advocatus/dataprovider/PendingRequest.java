package com.sahilpaudel.app.advocatus.dataprovider;

/**
 * Created by Sahil Paudel on 2/16/2017.
 */

public class PendingRequest {

    public String description;
    public String startTime;
    public String endTime;
    public String no_of_helpers;
    public String firstName;
    public String lastName;
    public String facebook_id;
    public String post_id;
    public String helper_id;

        @Override
        public String toString() {
            return firstName+"<>"+lastName+"<>"+description+"<>"+startTime+"<>"+endTime+"<>"+no_of_helpers+"<>"+facebook_id+"<>"+post_id;
        }

}
