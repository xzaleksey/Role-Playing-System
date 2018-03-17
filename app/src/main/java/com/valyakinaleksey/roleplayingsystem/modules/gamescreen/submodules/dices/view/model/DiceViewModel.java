package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model;

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
    private List<IFlexible<?>> diceItems = new ArrayList<>();

    public DiceViewModel() {
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setDiceItems(List<IFlexible<?>> diceItems) {
        this.diceItems = diceItems;
    }

    public List<IFlexible<?>> getDiceItems() {
        return diceItems;
    }

    @Override
    public boolean isUpdatedRequired() {
        return false;
    }
}
