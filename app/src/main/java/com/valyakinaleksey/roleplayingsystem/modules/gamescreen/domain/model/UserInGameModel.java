package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.google.firebase.database.Exclude;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.io.Serializable;

public class UserInGameModel implements Serializable {
    private String uid;
    private String name;

    @Exclude
    private User user;
    @Exclude
    private FirebaseChildEvent.EventType eventType;

    public UserInGameModel() {
    }

    public UserInGameModel(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FirebaseChildEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(FirebaseChildEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
      