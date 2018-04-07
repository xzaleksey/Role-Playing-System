package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceAdapter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceResult;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceType;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class SingleDiceTypeResultViewModel extends AbstractFlexibleItem<SingleDiceTypeResultViewHolder> {

    private List<DiceResult> diceResults;
    private int totalResultValue;
    private DiceType diceType;
    private boolean stateExpanded;

    public SingleDiceTypeResultViewModel(List<DiceResult> diceResults, int totalResultValue, DiceType diceType, boolean stateExpanded) {
        this.diceResults = diceResults;
        this.totalResultValue = totalResultValue;
        this.diceType = diceType;
        this.stateExpanded = stateExpanded;
    }

    public boolean isStateExpanded() {
        return stateExpanded;
    }

    public List<DiceResult> getDiceResults() {
        return diceResults;
    }

    public int getTotalResultValue() {
        return totalResultValue;
    }

    public DiceType getDiceType() {
        return diceType;
    }

    public void setStateExpanded(boolean stateExpanded) {
        this.stateExpanded = stateExpanded;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dice_result_item;
    }

    @Override
    public SingleDiceTypeResultViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SingleDiceTypeResultViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SingleDiceTypeResultViewHolder holder, int position, List payloads) {
        if (adapter instanceof DiceAdapter) {
            holder.bind(this, ((DiceAdapter) adapter).getDicePresenter());
        } else {
            throw new IllegalArgumentException("no DiceAdapter");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SingleDiceTypeResultViewModel that = (SingleDiceTypeResultViewModel) o;

        if (totalResultValue != that.totalResultValue) return false;
        return diceResults != null ? diceResults.equals(that.diceResults) : that.diceResults == null;
    }

    @Override
    public int hashCode() {
        int result = diceResults != null ? diceResults.hashCode() : 0;
        result = 31 * result + totalResultValue;
        return result;
    }
}

