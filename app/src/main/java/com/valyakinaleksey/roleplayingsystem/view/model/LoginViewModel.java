package com.valyakinaleksey.roleplayingsystem.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;

public class LoginViewModel implements EmptyViewModel, Parcelable, Serializable {

    private int temperature;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.temperature);
    }

    public LoginViewModel() {
    }

    private LoginViewModel(Parcel in) {
        this.temperature = in.readInt();
    }

    public static final Creator<LoginViewModel> CREATOR = new Creator<LoginViewModel>() {
        public LoginViewModel createFromParcel(Parcel source) {
            return new LoginViewModel(source);
        }

        public LoginViewModel[] newArray(int size) {
            return new LoginViewModel[size];
        }
    };
}
