package com.upec.androidtemplate20192020;

/**
 * Edited by Akram TOUABET on 22/03/2020 at 14:53
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private final static String PREFS_NAME = "app_prefs";
    private final static int PRIVATE_MODE = 0;
    private final static String IS_LOGGED = "isLogged";
    private final static String USERNAME = "username";
    private final static String EMAIL = "email";
    private final static String ID = "id";
    private Context context;

    public SessionManager(Context context){
        this.context=context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public boolean isLogged(){
        return prefs.getBoolean(IS_LOGGED, false);
    }

    public String getUsername(){
        return prefs.getString(USERNAME, null);
    }

    public String getEmail(){
        return prefs.getString(EMAIL, null);
    }

    public String getId(){
        return prefs.getString(ID, null);
    }

    public void insertUser(String id, String username, String email){
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(ID, id);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public void logout(){
        editor.clear().commit();
    }
}
