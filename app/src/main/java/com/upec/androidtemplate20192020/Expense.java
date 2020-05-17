package com.upec.androidtemplate20192020;

import java.util.ArrayList;

public class Expense {
    private String eid;
    private String mTitle;
    private String mAmount;
    private String mDate;
    private String mPayingUser;
    private ArrayList<String> mPayedForUsers;

    public Expense(String eid, String mTitle, String mAmount, String mDate, String mPayingUser) {
        this.eid = eid;
        this.mTitle = mTitle;
        this.mAmount = mAmount;
        this.mDate = mDate;
        this.mPayingUser = mPayingUser;
    }

    public String getEid() {
        return eid;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getmPayingUser() {
        return mPayingUser;
    }

    public void setmPayingUser(String mPayingUser) {
        this.mPayingUser = mPayingUser;
    }

    public ArrayList<String> getmPayedForUsers() {
        return mPayedForUsers;
    }
}
