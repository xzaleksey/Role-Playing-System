package com.valyakinaleksey.roleplayingsystem.utils;

public class FireBaseUtils {
    public static final String ID = "id";
    public static final String USERS = "users";
    public static final String GAMES = "games";
    public static final String FIELD_GAME_MASTER_NAME = "masterName";

    public static String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
      