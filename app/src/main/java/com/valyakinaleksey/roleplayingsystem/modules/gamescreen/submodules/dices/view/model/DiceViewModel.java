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
    private transient List<IFlexible<?>> diceCollectionsItems = new ArrayList<>();
    private transient List<IFlexible<?>> diceItems = new ArrayList<>();

    private DiceCollectionResult diceCollectionResult = new DiceCollectionResult();
    private DiceProgressState diceProgressState = DiceProgressState.IN_PROGRESS;
    private List<SingleDiceCollection> singleDiceCollections = new ArrayList<>();
    private List<DiceCollection> savedDiceCollections = new ArrayList<>();
    private DiceCollection selectedDiceCollection = null;

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

    public DiceCollection getSelectedDiceCollection() {
        return selectedDiceCollection;
    }

    public void setSelectedDiceCollection(DiceCollection selectedDiceCollection) {
        this.selectedDiceCollection = selectedDiceCollection;
    }

    public List<IFlexible<?>> getDiceCollectionsItems() {
        return diceCollectionsItems;
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

    public void setDiceCollectionResult(DiceCollectionResult diceCollectionResult) {
        this.diceCollectionResult = diceCollectionResult;
    }

    public List<IFlexible<?>> getDiceItems() {
        return diceItems;
    }

    public void setDiceItems(List<IFlexible<?>> diceItems) {
        this.diceItems = diceItems;
    }

    public List<SingleDiceCollection> getSingleDiceCollections() {
        return singleDiceCollections;
    }

    public void setSingleDiceCollections(List<SingleDiceCollection> singleDiceCollections) {
        this.singleDiceCollections = singleDiceCollections;
    }

    public List<DiceCollection> getSavedDiceCollections() {
        return savedDiceCollections;
    }

    public void setSavedDiceCollections(List<DiceCollection> savedDiceCollections) {
        this.savedDiceCollections = savedDiceCollections;
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
        dest.writeSerializable(this.diceCollectionResult);
        dest.writeInt(this.diceProgressState == null ? -1 : this.diceProgressState.ordinal());
        dest.writeList(this.singleDiceCollections);
        dest.writeList(this.savedDiceCollections);
        dest.writeSerializable(this.selectedDiceCollection);
    }

    protected DiceViewModel(Parcel in) {
        super(in);
        this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
        this.diceCollectionResult = (DiceCollectionResult) in.readSerializable();
        int tmpDiceProgressState = in.readInt();
        this.diceProgressState = tmpDiceProgressState == -1 ? null : DiceProgressState.values()[tmpDiceProgressState];
        this.singleDiceCollections = new ArrayList<SingleDiceCollection>();
        in.readList(this.singleDiceCollections, SingleDiceCollection.class.getClassLoader());
        this.savedDiceCollections = new ArrayList<DiceCollection>();
        in.readList(this.savedDiceCollections, DiceCollection.class.getClassLoader());
        this.selectedDiceCollection = (DiceCollection) in.readSerializable();
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
