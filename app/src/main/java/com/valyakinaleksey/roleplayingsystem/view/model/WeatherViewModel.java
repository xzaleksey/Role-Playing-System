package com.valyakinaleksey.roleplayingsystem.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;

public class WeatherViewModel implements EmptyViewModel, Parcelable, Serializable {

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

    public WeatherViewModel() {
    }

    private WeatherViewModel(Parcel in) {
        this.temperature = in.readInt();
    }

    public static final Creator<WeatherViewModel> CREATOR = new Creator<WeatherViewModel>() {
        public WeatherViewModel createFromParcel(Parcel source) {
            return new WeatherViewModel(source);
        }

        public WeatherViewModel[] newArray(int size) {
            return new WeatherViewModel[size];
        }
    };

    @Override
    public boolean isEmpty() {
        return false;
    }
}
