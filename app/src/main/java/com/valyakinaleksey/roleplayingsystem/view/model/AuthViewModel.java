package com.valyakinaleksey.roleplayingsystem.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;

public class AuthViewModel implements EmptyViewModel, Parcelable, Serializable {

    private String email;
    private String password;


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    public AuthViewModel() {
    }

    protected AuthViewModel(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Creator<AuthViewModel> CREATOR = new Creator<AuthViewModel>() {
        @Override
        public AuthViewModel createFromParcel(Parcel source) {
            return new AuthViewModel(source);
        }

        @Override
        public AuthViewModel[] newArray(int size) {
            return new AuthViewModel[size];
        }
    };
}
