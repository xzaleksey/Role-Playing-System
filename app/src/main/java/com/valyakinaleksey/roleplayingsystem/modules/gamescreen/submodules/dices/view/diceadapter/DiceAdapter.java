package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter;

import android.support.annotation.Nullable;

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class DiceAdapter extends FlexibleAdapter<IFlexible<?>> {
    private DicePresenter dicePresenter;

    public DiceAdapter(@Nullable List<IFlexible<?>> items, DicePresenter dicePresenter) {
        super(items);
        this.dicePresenter = dicePresenter;
    }

    public DicePresenter getDicePresenter() {
        return dicePresenter;
    }
}
