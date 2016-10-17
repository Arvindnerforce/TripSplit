package com.netforceinfotech.tripsplit.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {

    private static final String R_TOKEN = "rtoken";
    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    private static final String REG_ID = "regId";
    private static final String TOKEN = "token";
    private static final String FBID = "fbid";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String ISFIRSTTIME = "first_time";

    private static final String THEME = "theme";

    private static final String MUTEALL = "muteall";
    private static final String USER_ID = "user_id";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String regid) {
        editor.putString(USER_ID, regid);
        editor.commit();
    }

    public String getRegId() {
        return pref.getString(REG_ID, "");
    }

    public void setRegId(String regid) {
        editor.putString(REG_ID, regid);
        editor.commit();
    }


    public void setTeamNotification(String team_name, String sound) {
        editor.putString(team_name, sound);
        editor.commit();

    }

    public boolean getIsFirstTime() {
        return pref.getBoolean(ISFIRSTTIME, true);
    }

    public void setIsFirstTime(boolean regid) {
        editor.putBoolean(ISFIRSTTIME, regid);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void setToken(String regid) {
        editor.putString(TOKEN, regid);
        editor.commit();
    }

    public String getFBID() {

        return pref.getString(FBID, "");
    }

    public void setFBID(String regid) {
        editor.putString(FBID, regid);
        editor.commit();
    }

    public boolean getMuteAllNotification() {

        return pref.getBoolean(MUTEALL, false);
    }


    public void setMuteAllNotification(boolean mute) {

        editor.putBoolean(MUTEALL, mute);
        editor.commit();

    }

    public boolean getTeamNotification(String team_name) {
        return pref.getBoolean(team_name, false);

    }

    public void setTeamNotification(String team_name, boolean sound) {
        editor.putBoolean(team_name, sound);
        editor.commit();
    }


    public int getTheme() {

        return pref.getInt(THEME, 0);
    }

    public void setTheme(int theme) {
        editor.putInt(THEME, theme);
        editor.commit();
    }

    public String getEmail() {

        return pref.getString(EMAIL, "");
    }

    public void setEmail(String regid) {
        editor.putString(EMAIL, regid);
        editor.commit();
    }

    public String getName() {

        return pref.getString(NAME, "");
    }

    public void setName(String regid) {
        editor.putString(NAME, regid);
        editor.commit();
    }


}