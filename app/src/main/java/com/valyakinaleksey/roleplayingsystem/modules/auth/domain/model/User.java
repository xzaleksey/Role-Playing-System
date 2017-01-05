package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable, Parcelable {
    private String name;
    private String email;
    private String photoUrl;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public User(String name, String email, String photoUrl) {
        this(name, email);
        this.photoUrl = photoUrl;
    }

    public User() {
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.photoUrl);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.photoUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
      