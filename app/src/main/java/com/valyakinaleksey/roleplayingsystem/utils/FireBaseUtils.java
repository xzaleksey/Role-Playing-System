package com.valyakinaleksey.roleplayingsystem.utils;

public class FireBaseUtils {
    public static final String USERS ="users";
    public static String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
      