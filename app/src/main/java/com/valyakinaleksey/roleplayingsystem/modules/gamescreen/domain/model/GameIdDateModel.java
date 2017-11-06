package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;

public class GameIdDateModel implements HasId {
    private String id;
    private IdDateModel idDateModel;


    public GameIdDateModel(String id, IdDateModel idDateModel) {
        this.id = id;
        this.idDateModel = idDateModel;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public IdDateModel getIdDateModel() {
        return idDateModel;
    }
}
