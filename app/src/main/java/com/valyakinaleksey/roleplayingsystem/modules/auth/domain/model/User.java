package com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import java.io.Serializable;

public class User implements Serializable, Parcelable {
    public static final String FIELD_COUNT_OF_GAMES_PLAYED = "countOfGamesPlayed";
    public static final String FIELD_COUNT_OF_GAMES_MASTERED = "countOfGamesMastered";

    private String name;
    private String email;
    private String photoUrl;
    private String uid;
    private int countOfGamesPlayed;
    private int countOfGamesMastered;
    private String displayName;

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public User() {
    }

    public int getCountOfGamesPlayed() {
        return countOfGamesPlayed;
    }

    public void setCountOfGamesPlayed(int countOfGamesPlayed) {
        this.countOfGamesPlayed = countOfGamesPlayed;
    }

    public int getCountOfGamesMastered() {
        return countOfGamesMastered;
    }

    public void setCountOfGamesMastered(int countOfGamesMastered) {
        this.countOfGamesMastered = countOfGamesMastered;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        if (!StringUtils.isEmpty(displayName)) {
            return displayName;
        }
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
        dest.writeString(this.uid);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.photoUrl = in.readString();
        this.uid = in.readString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getUid().equals(user.getUid());
    }

    @Override
    public int hashCode() {
        return getUid().hashCode();
    }
}
      