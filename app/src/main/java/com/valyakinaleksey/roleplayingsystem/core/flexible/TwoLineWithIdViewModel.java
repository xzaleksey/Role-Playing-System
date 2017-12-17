package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.TwoLineWithIdViewHolder;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class TwoLineWithIdViewModel extends AbstractFlexibleItem<TwoLineWithIdViewHolder>
        implements Serializable {
    private String id;
    private String title;
    private String secondaryText;
    private String payload = StringUtils.EMPTY_STRING;

    public TwoLineWithIdViewModel(String id, String title, String secondaryText) {
        this.id = id;
        this.title = title;
        this.secondaryText = secondaryText;
    }

    public TwoLineWithIdViewModel(String id, String title, String secondaryText, String payload) {
        this.id = id;
        this.title = title;
        this.secondaryText = secondaryText;
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    @Override
    public TwoLineWithIdViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                    ViewGroup parent) {
        return new TwoLineWithIdViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, TwoLineWithIdViewHolder holder, int position,
                               List payloads) {
        holder.bind(title, secondaryText);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.two_or_three_lines_item_with_arrow_right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoLineWithIdViewModel that = (TwoLineWithIdViewModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (secondaryText != null ? !secondaryText.equals(that.secondaryText) : that.secondaryText != null)
            return false;
        return payload != null ? payload.equals(that.payload) : that.payload == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (secondaryText != null ? secondaryText.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }
}
      