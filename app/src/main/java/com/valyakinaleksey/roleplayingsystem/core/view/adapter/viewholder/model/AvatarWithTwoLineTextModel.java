package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.DrawableProvider;

import java.io.Serializable;

public class AvatarWithTwoLineTextModel implements Serializable, Parcelable {
    private String primaryText;
    private String secondaryText;
    private DrawableProvider placeHolderAndErrorDrawableProvider;
    private String photoUrl;
    private Serializable model;

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

    public DrawableProvider getPlaceHolderAndErrorDrawableProvider() {
        return placeHolderAndErrorDrawableProvider;
    }

    public void setPlaceHolderAndErrorDrawableProvider(DrawableProvider placeHolderAndErrorDrawableProvider) {
        this.placeHolderAndErrorDrawableProvider = placeHolderAndErrorDrawableProvider;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public AvatarWithTwoLineTextModel() {
    }

    public AvatarWithTwoLineTextModel(String primaryText, String secondaryText, DrawableProvider placeHolderAndErrorDrawableProvider, String photoUrl, Serializable model) {
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.placeHolderAndErrorDrawableProvider = placeHolderAndErrorDrawableProvider;
        this.photoUrl = photoUrl;
        this.model = model;
    }

    public Serializable getModel() {
        return model;
    }

    public void setModel(Serializable model) {
        this.model = model;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.primaryText);
        dest.writeString(this.secondaryText);
        dest.writeSerializable(this.placeHolderAndErrorDrawableProvider);
        dest.writeString(this.photoUrl);
        dest.writeSerializable(this.model);
    }

    protected AvatarWithTwoLineTextModel(Parcel in) {
        this.primaryText = in.readString();
        this.secondaryText = in.readString();
        this.placeHolderAndErrorDrawableProvider = (DrawableProvider) in.readSerializable();
        this.photoUrl = in.readString();
        this.model = in.readSerializable();
    }

    public static final Creator<AvatarWithTwoLineTextModel> CREATOR = new Creator<AvatarWithTwoLineTextModel>() {
        @Override
        public AvatarWithTwoLineTextModel createFromParcel(Parcel source) {
            return new AvatarWithTwoLineTextModel(source);
        }

        @Override
        public AvatarWithTwoLineTextModel[] newArray(int size) {
            return new AvatarWithTwoLineTextModel[size];
        }
    };
}
      