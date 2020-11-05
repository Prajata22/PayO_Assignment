package com.applex.payo_assignment.SharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class IntroPref {

    private SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private static final String PREF_NAME = "com.applex.payo";
    private static final String IS_LOGGED_IN = "loginStatus";
    private static final String CONTACT = "contact";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";
    private static final String NAME= "name";
    private static final String EMAIL = "email";

    @SuppressLint("CommitPrefEdits")
    public IntroPref(Context context) {
        if(context != null) {
            preferences = context.getSharedPreferences(PREF_NAME, 0);
        }
        editor = preferences.edit();
    }

    public void setIsLoggedIn(boolean firstTimeLaunch) {
        editor.putBoolean(IS_LOGGED_IN,firstTimeLaunch);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getName(){
        return preferences.getString(NAME, null);
    }

    public void setName(String gender){
        editor.putString(NAME, gender);
        editor.apply();
    }

    public String getEmail(){
        return preferences.getString(EMAIL, null);
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getContact(){
        return preferences.getString(CONTACT, null);
    }

    public void setContact(String contact){
        editor.putString(CONTACT, contact);
        editor.apply();
    }

    public String getAddress(){
        return preferences.getString(ADDRESS, null);
    }

    public void setAddress(String address){
        editor.putString(ADDRESS, address);
        editor.apply();
    }

    public String getPassword(){
        return preferences.getString(PASSWORD, null);
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD, password);
        editor.apply();
    }
}