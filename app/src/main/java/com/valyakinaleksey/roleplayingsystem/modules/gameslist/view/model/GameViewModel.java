package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.io.Serializable;

public class GameViewModel implements Parcelable, Serializable {
    private GameModel gameModel;

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.gameModel, flags);
    }

    public GameViewModel() {
    }

    protected GameViewModel(Parcel in) {
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    }

    public static final Creator<GameViewModel> CREATOR = new Creator<GameViewModel>() {
        @Override
        public GameViewModel createFromParcel(Parcel source) {
            return new GameViewModel(source);
        }

        @Override
        public GameViewModel[] newArray(int size) {
            return new GameViewModel[size];
        }
    };
}
      