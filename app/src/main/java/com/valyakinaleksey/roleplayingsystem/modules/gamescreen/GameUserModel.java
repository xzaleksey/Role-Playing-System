package com.valyakinaleksey.roleplayingsystem.modules.gamescreen;

import java.io.Serializable;

public class GameUserModel implements Serializable {
    private String uid;

    public GameUserModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
      