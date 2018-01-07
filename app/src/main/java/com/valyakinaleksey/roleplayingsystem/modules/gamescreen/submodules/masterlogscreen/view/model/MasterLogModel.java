package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

public class MasterLogModel extends BaseRequestUpdateViewModel
        implements RequestUpdateViewModel, Parcelable, Serializable {

    private GameModel gameModel;
    private transient List<IFlexible<?>> items = new ArrayList<>();

    public MasterLogModel() {
    }

    public List<IFlexible<?>> getItems() {
        return items;
    }

    public void setItems(List<IFlexible<?>> items) {
        this.items = items;
    }

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
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.gameModel, flags);
    }

    protected MasterLogModel(Parcel in) {
        super(in);
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    }

    public static final Creator<MasterLogModel> CREATOR = new Creator<MasterLogModel>() {
        @Override
        public MasterLogModel createFromParcel(Parcel source) {
            return new MasterLogModel(source);
        }

        @Override
        public MasterLogModel[] newArray(int size) {
            return new MasterLogModel[size];
        }
    };
}
