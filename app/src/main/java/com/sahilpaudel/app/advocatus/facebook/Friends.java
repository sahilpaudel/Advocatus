package com.sahilpaudel.app.advocatus.facebook;

/**
 * Created by Sahil Paudel on 2/10/2017.
 */

public class Friends {
    private String friendName;
    private String friendID;
    private String imageURL;

    public Friends() {
    }

    public Friends(String friendName, String friendID) {
        this.friendName = friendName;
        this.friendID = friendID;
    }

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
