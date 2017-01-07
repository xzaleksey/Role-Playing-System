package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoSectionImpl<T extends Serializable> implements InfoSection<T> {
    private int sectionType;
    private boolean active = true;
    protected ArrayList<T> data;
    private String title;

    public InfoSectionImpl(int sectionType, String title, List<T> data) {
        this(sectionType, data);
        this.title = title;
    }

    public InfoSectionImpl(int sectionType, List<T> data) {
        this.sectionType = sectionType;
        this.data = new ArrayList<>(data);
    }

    @Override
    public int getSectionType() {
        return sectionType;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public ArrayList<T> getData() {
        return data;
    }

    @Override
    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public String getTitle() {
        return title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sectionType);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.data);
        dest.writeString(this.title);
    }

    @SuppressWarnings("unchecked")
    protected InfoSectionImpl(Parcel in) {
        this.sectionType = in.readInt();
        this.active = in.readByte() != 0;
        this.data = (ArrayList<T>) in.readSerializable();
        this.title = in.readString();
    }

    @Override
    public int getItemCount() {
        return data.size() + (title != null ? 1 : 0);
    }
}
