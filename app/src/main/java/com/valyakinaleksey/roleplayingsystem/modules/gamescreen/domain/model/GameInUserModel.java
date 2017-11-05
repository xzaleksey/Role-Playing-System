package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameInUserModel implements Serializable {
    private Object lastVisitedDate = ServerValue.TIMESTAMP;
    public static final String FIELD_LAST_VISITED_DATE = "lastVisitedDate";

    @Exclude
    private User user;

    public GameInUserModel() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Exclude
    public long getLastVisitedLong() {
        if (lastVisitedDate instanceof Long) {
            return (long) lastVisitedDate;
        }
        return System.currentTimeMillis();
    }

    public Object getLastVisitedDate() {
        return lastVisitedDate;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put(FIELD_LAST_VISITED_DATE, lastVisitedDate);
        return result;
    }
}
      