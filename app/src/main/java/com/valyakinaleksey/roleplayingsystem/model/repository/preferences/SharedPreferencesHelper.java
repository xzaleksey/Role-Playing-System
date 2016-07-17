package com.valyakinaleksey.roleplayingsystem.model.repository.preferences;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

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
                .commit();
    }

    public String getLogin() {
        return preferences.getString(LOGIN, "");
    }

    public void savePassword(String password) {
        preferences
                .edit()
                .putString(PASSWORD, password)
                .commit();
    }

    public String getPassword() {
        return preferences.getString(PASSWORD, "");
    }
}
      