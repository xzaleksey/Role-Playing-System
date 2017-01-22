package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ExpandableTitleViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.TwoOrThreeLineViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;

import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_EXPANDABLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_OR_THREE_LINE_ITEM;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_EXPANDABLE_TITLE;

public class DefaultExpandableSectionImpl extends ExpandableSection<TwoOrThreeLineTextModel> {
    public DefaultExpandableSectionImpl(String title, List<TwoOrThreeLineTextModel> data) {
        super(ELEMENT_TYPE_EXPANDABLE, title, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TYPE_EXPANDABLE_TITLE:
                v = inflater.inflate(R.layout.title_item_with_right_image, parent, false);
                viewHolder = new ExpandableTitleViewHolder(v);
                break;
            case TYPE_TWO_OR_THREE_LINE_ITEM:
                v = inflater.inflate(R.layout.two_or_three_lines_item, parent, false);
                viewHolder = new TwoOrThreeLineViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, RecyclerView.Adapter adapter) {
        switch (holder.getItemViewType()) {
            case TYPE_EXPANDABLE_TITLE:
                ((ExpandableTitleViewHolder) holder).bind(getTitle(), this, adapter);
                break;
            case TYPE_TWO_OR_THREE_LINE_ITEM:
                ((TwoOrThreeLineViewHolder) holder).bind(data.get(position - 1));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_EXPANDABLE_TITLE;
        }
        return TYPE_TWO_OR_THREE_LINE_ITEM;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected DefaultExpandableSectionImpl(Parcel in) {
        super(in);
    }

    public static final Creator<DefaultExpandableSectionImpl> CREATOR = new Creator<DefaultExpandableSectionImpl>() {
        @Override
        public DefaultExpandableSectionImpl createFromParcel(Parcel source) {
            return new DefaultExpandableSectionImpl(source);
        }

        @Override
        public DefaultExpandableSectionImpl[] newArray(int size) {
            return new DefaultExpandableSectionImpl[size];
        }
    };

    @Override
    public int getItemCount() {
        return expanded ? super.getItemCount() : 1;
    }
}
      