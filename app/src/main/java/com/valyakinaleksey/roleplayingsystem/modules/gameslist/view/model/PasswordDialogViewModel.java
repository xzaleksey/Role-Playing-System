package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.io.Serializable;

public class PasswordDialogViewModel implements Serializable, Parcelable {
    private String title;
    private GameModel gameModel;
    private String inputPassword;

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

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public PasswordDialogViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeParcelable(this.gameModel, flags);
        dest.writeString(this.inputPassword);
    }

    protected PasswordDialogViewModel(Parcel in) {
        this.title = in.readString();
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
        this.inputPassword = in.readString();
    }

    public static final Creator<PasswordDialogViewModel> CREATOR = new Creator<PasswordDialogViewModel>() {
        @Override
        public PasswordDialogViewModel createFromParcel(Parcel source) {
            return new PasswordDialogViewModel(source);
        }

        @Override
        public PasswordDialogViewModel[] newArray(int size) {
            return new PasswordDialogViewModel[size];
        }
    };
}
      