package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;
import java.util.List;

public class GamesListViewModel implements EmptyViewModel, Parcelable, Serializable {

    private List<GameViewModel> gameViewModels;

    public GamesListViewModel() {
    }

    public List<GameViewModel> getGameViewModels() {
        return gameViewModels;
    }

    public void setGameViewModels(List<GameViewModel> gameViewModels) {
        this.gameViewModels = gameViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.gameViewModels);
    }

    protected GamesListViewModel(Parcel in) {
        this.gameViewModels = in.createTypedArrayList(GameViewModel.CREATOR);
    }

    public static final Creator<GamesListViewModel> CREATOR = new Creator<GamesListViewModel>() {
        @Override
        public GamesListViewModel createFromParcel(Parcel source) {
            return new GamesListViewModel(source);
        }

        @Override
        public GamesListViewModel[] newArray(int size) {
            return new GamesListViewModel[size];
        }
    };

    @Override
    public boolean isEmpty() {
        return gameViewModels == null || gameViewModels.isEmpty();
    }
}
