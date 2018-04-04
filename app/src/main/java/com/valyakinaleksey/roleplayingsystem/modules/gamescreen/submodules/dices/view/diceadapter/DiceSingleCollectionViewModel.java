package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.SingleDiceCollection;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class DiceSingleCollectionViewModel extends AbstractFlexibleItem<DiceSingleCollectionViewHolder> {

    private int mainDiceResId;
    private SingleDiceCollection diceCollection;

    public DiceSingleCollectionViewModel(int mainDiceResId, SingleDiceCollection diceCollection) {
        this.mainDiceResId = mainDiceResId;
        this.diceCollection = diceCollection;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dice_element;
    }

    public int getMainDiceResId() {
        return mainDiceResId;
    }

    public SingleDiceCollection getDiceCollection() {
        return diceCollection;
    }

    @Override
    public DiceSingleCollectionViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DiceSingleCollectionViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DiceSingleCollectionViewHolder holder, int position, List payloads) {
        if (adapter instanceof DiceAdapter) {
            holder.bind(this, ((DiceAdapter) adapter).getDicePresenter());
        } else {
            throw new IllegalArgumentException("adapter should be diceAdapter");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiceSingleCollectionViewModel that = (DiceSingleCollectionViewModel) o;

        if (mainDiceResId != that.mainDiceResId) return false;
        return diceCollection != null ? diceCollection.equals(that.diceCollection) : that.diceCollection == null;
    }

    @Override
    public int hashCode() {
        int result = mainDiceResId;
        result = 31 * result + (diceCollection != null ? diceCollection.hashCode() : 0);
        return result;
    }
}
