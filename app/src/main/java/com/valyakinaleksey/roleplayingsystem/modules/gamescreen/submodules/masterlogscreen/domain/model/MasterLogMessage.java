package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MasterLogMessage implements Serializable, Parcelable {
    public static final int SIMPLE_LINK = 0;
    public static final int IMAGE_LINK = 1;

    private String id;
    private String text;
    private long dateCreate;
    private String link;
    private int linkType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(long dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.text);
        dest.writeLong(this.dateCreate);
        dest.writeString(this.link);
        dest.writeInt(this.linkType);
    }

    public MasterLogMessage() {
    }

    protected MasterLogMessage(Parcel in) {
        this.id = in.readString();
        this.text = in.readString();
        this.dateCreate = in.readLong();
        this.link = in.readString();
        this.linkType = in.readInt();
    }

    public static final Creator<MasterLogMessage> CREATOR = new Creator<MasterLogMessage>() {
        @Override
        public MasterLogMessage createFromParcel(Parcel source) {
            return new MasterLogMessage(source);
        }

        @Override
        public MasterLogMessage[] newArray(int size) {
            return new MasterLogMessage[size];
        }
    };
}
      