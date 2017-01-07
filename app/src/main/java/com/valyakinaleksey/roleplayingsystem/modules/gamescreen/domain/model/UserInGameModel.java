package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import java.io.Serializable;

public class UserInGameModel implements Serializable {
    private String uid;

    public UserInGameModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
      