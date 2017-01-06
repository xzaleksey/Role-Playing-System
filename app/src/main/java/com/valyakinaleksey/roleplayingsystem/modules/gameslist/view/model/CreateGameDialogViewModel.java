package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.io.Serializable;

public class CreateGameDialogViewModel implements Serializable, Parcelable {
    private String title;
    private GameModel gameModel;

    public String getTitle() {
        return title;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CreateGameDialogViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeParcelable(this.gameModel, flags);
    }

    protected CreateGameDialogViewModel(Parcel in) {
        this.title = in.readString();
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    }

    public static final Creator<CreateGameDialogViewModel> CREATOR = new Creator<CreateGameDialogViewModel>() {
        @Override
        public CreateGameDialogViewModel createFromParcel(Parcel source) {
            return new CreateGameDialogViewModel(source);
        }

        @Override
        public CreateGameDialogViewModel[] newArray(int size) {
            return new CreateGameDialogViewModel[size];
        }
    };
}
      