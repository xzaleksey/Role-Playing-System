package com.valyakinaleksey.roleplayingsystem.core.flexible;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.SubHeaderViewHolder;
import com.valyakinaleksey.roleplayingsystem.utils.extensions.ContextExtensions;

import java.io.Serializable;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class SubHeaderViewModel extends AbstractFlexibleItem<SubHeaderViewHolder> implements Serializable {
    private String title;
    private int color;
    private boolean drawBottomDivider;
    private boolean drawTopDivider;
    private int paddingLeft;

    public SubHeaderViewModel() {
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public SubHeaderViewModel(String title) {
        this.title = title;
    }

    public SubHeaderViewModel(String title, boolean drawBottomDivider) {
        this(title);
        this.drawBottomDivider = drawBottomDivider;
    }

    public SubHeaderViewModel(String title, boolean drawBottomDivider, int color) {
        this(title, drawBottomDivider);
        this.color = color;
    }

    private SubHeaderViewModel(Builder builder) {
        setTitle(builder.title);
        color = builder.color;
        drawBottomDivider = builder.drawBottomDivider;
        setDrawTopDivider(builder.drawTopDivider);
        paddingLeft = builder.paddingLeft;
    }

    public boolean isDrawBottomDivider() {
        return drawBottomDivider;
    }

    public boolean isDrawTopDivider() {
        return drawTopDivider;
    }

    public void setDrawTopDivider(boolean drawTopDivider) {
        this.drawTopDivider = drawTopDivider;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public SubHeaderViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
                                                ViewGroup parent) {
        return new SubHeaderViewHolder(inflater.inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, SubHeaderViewHolder holder, int position,
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
        if (drawBottomDivider != that.drawBottomDivider) return false;
        if (drawTopDivider != that.drawTopDivider) return false;
        return title != null ? title.equals(that.title) : that.title == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + color;
        result = 31 * result + (drawBottomDivider ? 1 : 0);
        result = 31 * result + (drawTopDivider ? 1 : 0);
        return result;
    }


    public static final class Builder {
        private String title;
        private int color;
        private boolean drawBottomDivider;
        private boolean drawTopDivider;
        private int paddingLeft = ContextExtensions.convertDpToPixel(16);

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder color(int val) {
            color = val;
            return this;
        }

        public Builder drawBottomDivider(boolean val) {
            drawBottomDivider = val;
            return this;
        }

        public Builder paddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        public Builder drawTopDivider(boolean val) {
            drawTopDivider = val;
            return this;
        }

        public SubHeaderViewModel build() {
            return new SubHeaderViewModel(this);
        }
    }
}
      