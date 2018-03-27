package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

public class DiceViewModel extends BaseRequestUpdateViewModel implements RequestUpdateViewModel, Parcelable, Serializable {

    private GameModel gameModel;
    private DiceCollection diceCollection = new DiceCollection();
    private transient List<IFlexible<?>> diceCollectionsItems = new ArrayList<>();
    private DiceProgressState diceProgressState = DiceProgressState.IN_PROGRESS;
    private DiceCollectionResult diceCollectionResult = new DiceCollectionResult();

    public DiceViewModel() {
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setDiceCollectionsItems(List<IFlexible<?>> diceCollectionsItems) {
        this.diceCollectionsItems = diceCollectionsItems;
    }

    public List<IFlexible<?>> getDiceCollectionsItems() {
        return diceCollectionsItems;
    }

    public DiceCollection getDiceCollection() {
        return diceCollection;
    }

    public void setDiceProgressState(DiceProgressState diceProgressState) {
        this.diceProgressState = diceProgressState;
    }

    public DiceProgressState getDiceProgressState() {
        return diceProgressState;
    }

    public DiceCollectionResult getDiceCollectionResult() {
        return diceCollectionResult;
    }

    public void setDiceCollection(DiceCollection diceCollection) {
        this.diceCollection = diceCollection;
    }

    @Override
    public boolean isUpdatedRequired() {
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.gameModel, flags);
        dest.writeSerializable(this.diceCollection);
        dest.writeInt(this.diceProgressState == null ? -1 : this.diceProgressState.ordinal());
        dest.writeSerializable(this.diceCollectionResult);
    }

    protected DiceViewModel(Parcel in) {
        super(in);
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
        this.diceCollection = (DiceCollection) in.readSerializable();
        int tmpDiceProgressState = in.readInt();
        this.diceProgressState = tmpDiceProgressState == -1 ? null : DiceProgressState.values()[tmpDiceProgressState];
        this.diceCollectionResult = (DiceCollectionResult) in.readSerializable();
    }

    public static final Creator<DiceViewModel> CREATOR = new Creator<DiceViewModel>() {
        @Override
        public DiceViewModel createFromParcel(Parcel source) {
            return new DiceViewModel(source);
        }

        @Override
        public DiceViewModel[] newArray(int size) {
            return new DiceViewModel[size];
        }
    };
}
