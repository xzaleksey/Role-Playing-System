package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.util.List;

public class DiceTotalResultViewModel extends AbstractFlexibleItem<DiceTotalResultViewHolder> {

    private String currentResultValue;
    private String maxResultValue;

    @Override
    public int getLayoutRes() {
        return R.layout.dice_total_result_element;
    }

    public String getCurrentResultValue() {
        return currentResultValue;
    }

    public String getMaxResultValue() {
        return maxResultValue;
    }

    @Override
    public DiceTotalResultViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DiceTotalResultViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DiceTotalResultViewHolder holder, int position, List payloads) {
        holder.bind(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiceTotalResultViewModel that = (DiceTotalResultViewModel) o;

        if (currentResultValue != null ? !currentResultValue.equals(that.currentResultValue) : that.currentResultValue != null)
            return false;
        return maxResultValue != null ? maxResultValue.equals(that.maxResultValue) : that.maxResultValue == null;
    }

    @Override
    public int hashCode() {
        int result = currentResultValue != null ? currentResultValue.hashCode() : 0;
        result = 31 * result + (maxResultValue != null ? maxResultValue.hashCode() : 0);
        return result;
    }

}

