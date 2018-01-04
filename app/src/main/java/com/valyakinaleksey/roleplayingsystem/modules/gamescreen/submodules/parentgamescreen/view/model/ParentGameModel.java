package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model;

import android.os.Parcel;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import static com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen.GAME_CHARACTERS_FRAGMENT;

public class ParentGameModel extends BaseRequestUpdateViewModel {

    private GameModel gameModel;
    private boolean isMaster;
    private int navigationTag = GAME_CHARACTERS_FRAGMENT;
    private boolean firstNavigation = true;

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public boolean isFirstNavigation() {
        return firstNavigation;
    }

    public void setFirstNavigation(boolean firstNavigation) {
        this.firstNavigation = firstNavigation;
    }

    @Override
    public boolean isUpdatedRequired() {
        return firstNavigation;
    }

    public int getNavigationTag() {
        return navigationTag;
    }

    public void setNavigationTag(int navigationTag) {
        this.navigationTag = navigationTag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.gameModel, flags);
        dest.writeByte(this.isMaster ? (byte) 1 : (byte) 0);
        dest.writeInt(this.navigationTag);
        dest.writeByte(this.firstNavigation ? (byte) 1 : (byte) 0);
    }

    public ParentGameModel() {
    }

    protected ParentGameModel(Parcel in) {
        super(in);
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
        this.isMaster = in.readByte() != 0;
        this.navigationTag = in.readInt();
        this.firstNavigation = in.readByte() != 0;
    }

    public static final Creator<ParentGameModel> CREATOR = new Creator<ParentGameModel>() {
        @Override
        public ParentGameModel createFromParcel(Parcel source) {
            return new ParentGameModel(source);
        }

        @Override
        public ParentGameModel[] newArray(int size) {
            return new ParentGameModel[size];
        }
    };
}
