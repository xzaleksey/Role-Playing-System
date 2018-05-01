package com.valyakinaleksey.roleplayingsystem.utils;

import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

    private SharedPreferences preferences;

    public SharedPreferencesHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveLogin(String login) {
        preferences
                .edit()
                .putString(LOGIN, login)
                .apply();
    }

    public String getLogin() {
        return preferences.getString(LOGIN, "");
    }

}
      