package com.sahilpaudel.app.advocatus;

/**
 * Created by Sahil Paudel on 2/13/2017.
 */

public class Feeds {
    private String mUserName;
    private String mContent;
    private String mStartTime;
    private String mLimit;
    private int helpers;

    public Feeds() {
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmLimit() {
        return mLimit;
    }

    public void setmLimit(String mLimit) {
        this.mLimit = mLimit;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public int getHelpers() {
        return helpers;
    }

    public void setHelpers(int helpers) {
        this.helpers = helpers;
    }
}
