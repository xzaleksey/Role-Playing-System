package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TwoOrThreeLineTextModel implements Serializable, Parcelable {
    private String primaryText;
    private String secondaryText;

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.primaryText);
        dest.writeString(this.secondaryText);
    }

    public TwoOrThreeLineTextModel() {
    }

    public TwoOrThreeLineTextModel(String primaryText, String secondaryText) {
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
    }

    protected TwoOrThreeLineTextModel(Parcel in) {
        this.primaryText = in.readString();
        this.secondaryText = in.readString();
    }

    public static final Creator<TwoOrThreeLineTextModel> CREATOR = new Creator<TwoOrThreeLineTextModel>() {
        @Override
        public TwoOrThreeLineTextModel createFromParcel(Parcel source) {
            return new TwoOrThreeLineTextModel(source);
        }

        @Override
        public TwoOrThreeLineTextModel[] newArray(int size) {
            return new TwoOrThreeLineTextModel[size];
        }
    };
}
      