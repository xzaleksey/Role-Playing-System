package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.SubheaderViewHolder;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

import java.io.Serializable;
import java.util.List;

public class SubHeaderViewModel extends AbstractFlexibleItem<SubheaderViewHolder> implements
        Serializable {
    private String title;
    private int color;
    private boolean drawTopDivider;

    public SubHeaderViewModel() {
    }

    public SubHeaderViewModel(String title) {
        this.title = title;
    }

    public SubHeaderViewModel(String title, boolean drawTopDivider) {
        this(title);
        this.drawTopDivider = drawTopDivider;
    }

    public SubHeaderViewModel(String title, boolean drawTopDivider, int color) {
        this(title, drawTopDivider);
        this.color = color;
    }

    public boolean isDrawTopDivider() {
        return drawTopDivider;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public SubheaderViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                ViewGroup parent) {
        return new SubheaderViewHolder(inflater.inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SubheaderViewHolder holder, int position,
                               List payloads) {
        holder.bind(this);
    }

    public int getColor() {
        return color;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.simple_header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubHeaderViewModel that = (SubHeaderViewModel) o;

        if (color != that.color) return false;
        if (drawTopDivider != that.drawTopDivider) return false;
        return title != null ? title.equals(that.title) : that.title == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + color;
        result = 31 * result + (drawTopDivider ? 1 : 0);
        return result;
    }
}
      