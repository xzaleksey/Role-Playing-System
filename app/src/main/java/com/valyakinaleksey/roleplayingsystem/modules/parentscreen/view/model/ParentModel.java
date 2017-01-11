package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.io.Serializable;

public class ParentModel implements EmptyViewModel, Parcelable, Serializable {

    private int navigationTag;
    private boolean firstNavigation = true;

    public int getNavigationId() {
        return navigationTag;
    }

    public void setNavigationTag(int navigationTag) {
        this.navigationTag = navigationTag;
    }

    public boolean isFirstNavigation() {
        return firstNavigation;
    }

    public void setFirstNavigation(boolean firstNavigation) {
        this.firstNavigation = firstNavigation;
    }

    public ParentModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.navigationTag);
        dest.writeByte(this.firstNavigation ? (byte) 1 : (byte) 0);
    }

    protected ParentModel(Parcel in) {
        this.navigationTag = in.readInt();
        this.firstNavigation = in.readByte() != 0;
    }

    public static final Creator<ParentModel> CREATOR = new Creator<ParentModel>() {
        @Override
        public ParentModel createFromParcel(Parcel source) {
            return new ParentModel(source);
        }

        @Override
        public ParentModel[] newArray(int size) {
            return new ParentModel[size];
        }
    };

    @Override
    public boolean isEmpty() {
        return firstNavigation;
    }
}
