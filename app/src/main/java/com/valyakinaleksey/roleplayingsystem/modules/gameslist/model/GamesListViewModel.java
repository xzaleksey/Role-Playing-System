package com.valyakinaleksey.roleplayingsystem.modules.gameslist.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;

public class GamesListViewModel implements EmptyViewModel, Parcelable, Serializable {

    private String email ="";
    private String password ="";
    private FirebaseUser firebaseUser;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GamesListViewModel() {
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    protected GamesListViewModel(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Creator<GamesListViewModel> CREATOR = new Creator<GamesListViewModel>() {
        @Override
        public GamesListViewModel createFromParcel(Parcel source) {
            return new GamesListViewModel(source);
        }

        @Override
        public GamesListViewModel[] newArray(int size) {
            return new GamesListViewModel[size];
        }
    };

    @Override
    public boolean isEmpty() {
        return false;
    }
}
