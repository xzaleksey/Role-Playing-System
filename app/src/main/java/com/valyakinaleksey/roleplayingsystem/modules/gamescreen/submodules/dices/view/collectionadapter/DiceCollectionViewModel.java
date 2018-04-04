package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.collectionadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class DiceCollectionViewModel extends AbstractFlexibleItem<DiceCollectionViewHolder> {

    private DiceCollection diceCollection;
    private boolean isSelected;

    public DiceCollectionViewModel(DiceCollection diceCollection, boolean isSelected) {
        this.diceCollection = diceCollection;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dice_collection_element;
    }

    public DiceCollection getDiceCollection() {
        return diceCollection;
    }

    @Override
    public DiceCollectionViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DiceCollectionViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DiceCollectionViewHolder holder, int position, List payloads) {
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

        DiceCollectionViewModel that = (DiceCollectionViewModel) o;

        return diceCollection != null ? diceCollection.equals(that.diceCollection) : that.diceCollection == null;
    }

    @Override
    public int hashCode() {
        return diceCollection != null ? diceCollection.hashCode() : 0;
    }
}
