package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDate;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;

import java.io.Serializable;

public class IdDateModel implements HasId, HasDate, Serializable {
    public static final String DATE_VISITED = "dateVisited";
    private String id;
    private long date;
    private Object dateVisited = ServerValue.TIMESTAMP;

    public IdDateModel() {
    }

    public IdDateModel(String id, long date) {
        this.id = id;
        this.date = date;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public void setDate(long date) {
        this.date = date;
    }

    public Object getDateVisited() {
        return dateVisited;
    }

    @Exclude
    public long getDateVisitedLong() {
        if (dateVisited == null || dateVisited == ServerValue.TIMESTAMP) {
            return date;
        }
        return (long) dateVisited;
    }
}
