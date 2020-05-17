package com.upec.androidtemplate20192020;

public class SplitCount{
    private String mName;
    private String mDescription;
    private int mSid;
    private String mCode;

    public SplitCount(String mCode, String mName, String mDescription, int mSid) {
        this.mCode = mCode;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mSid=mSid;

    }

    public String getmName() {
        return mName;
    }

    public int getmSid() {
        return mSid;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmCode() {
        return mCode;
    }

}
