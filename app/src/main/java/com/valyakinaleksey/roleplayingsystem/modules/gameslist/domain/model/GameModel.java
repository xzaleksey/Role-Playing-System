package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GameModel implements Serializable, Parcelable {
    private String id;
    private String masterId;
    private String name;
    private long dateCreate;
    private String description;

    public GameModel() {
    }

    public GameModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GameModel(String id, String masterId, String name, long dateCreate, String description) {
        this(masterId, name, dateCreate, description);
        this.id = id;
    }

    public GameModel(String masterId, String name, long dateCreate, String description) {
        this(name, description);
        this.masterId = masterId;
        this.dateCreate = dateCreate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(long dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.masterId);
        dest.writeString(this.name);
        dest.writeLong(this.dateCreate);
        dest.writeString(this.description);
    }

    protected GameModel(Parcel in) {
        this.id = in.readString();
        this.masterId = in.readString();
        this.name = in.readString();
        this.dateCreate = in.readLong();
        this.description = in.readString();
    }

    public static final Creator<GameModel> CREATOR = new Creator<GameModel>() {
        @Override
        public GameModel createFromParcel(Parcel source) {
            return new GameModel(source);
        }

        @Override
        public GameModel[] newArray(int size) {
            return new GameModel[size];
        }
    };
}
      