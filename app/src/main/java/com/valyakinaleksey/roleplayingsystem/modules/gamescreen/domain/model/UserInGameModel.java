package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import java.io.Serializable;

public class UserInGameModel implements Serializable {
    private String uid;
    private String name;

    public UserInGameModel() {
    }

    public UserInGameModel(String uid, String name) {
        this.uid = uid;
        this.name = name;
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
      