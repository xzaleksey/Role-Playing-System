package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;

public class MasterModel implements EmptyViewModel, Parcelable, Serializable {

    private String toolbarTitle;
    private GameModel gameModel;
    private ArrayList<InfoSection> infoSections;

    public MasterModel() {
    }

    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public ArrayList<InfoSection> getInfoSections() {
        return infoSections;
    }

    public void setInfoSections(ArrayList<InfoSection> infoSections) {
        this.infoSections = infoSections;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toolbarTitle);
        dest.writeParcelable(this.gameModel, flags);
        dest.writeSerializable(this.infoSections);
    }

    protected MasterModel(Parcel in) {
        this.toolbarTitle = in.readString();
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
        this.infoSections = (ArrayList<InfoSection>) in.readSerializable();

    }

    public static final Creator<MasterModel> CREATOR = new Creator<MasterModel>() {
        @Override
        public MasterModel createFromParcel(Parcel source) {
            return new MasterModel(source);
        }

        @Override
        public MasterModel[] newArray(int size) {
            return new MasterModel[size];
        }
    };
}
