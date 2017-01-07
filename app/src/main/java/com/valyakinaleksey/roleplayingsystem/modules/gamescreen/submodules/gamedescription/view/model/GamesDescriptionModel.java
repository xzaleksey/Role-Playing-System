package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;

public class GamesDescriptionModel implements EmptyViewModel, Parcelable, Serializable {


    private String toolbarTitle;

    public GamesDescriptionModel() {
    }

    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toolbarTitle);
    }

    protected GamesDescriptionModel(Parcel in) {
        this.toolbarTitle = in.readString();
    }

    public static final Creator<GamesDescriptionModel> CREATOR = new Creator<GamesDescriptionModel>() {
        @Override
        public GamesDescriptionModel createFromParcel(Parcel source) {
            return new GamesDescriptionModel(source);
        }

        @Override
        public GamesDescriptionModel[] newArray(int size) {
            return new GamesDescriptionModel[size];
        }
    };

    @Override
    public boolean isEmpty() {
        return false;
    }
}
