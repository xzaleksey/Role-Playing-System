package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.AvatarTwoLineTextViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.AvatarWithTwoLineTextModel;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.DividerViewHolder;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.TitleViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.adapter.DescriptionViewHolder;

import java.io.Serializable;
import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_STATIC_SECTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DIVIDER;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TITLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_LINE_WITH_AVATAR;

public class StaticFieldsSection extends InfoSectionImpl<StaticItem> {

    public StaticFieldsSection(List<StaticItem> data) {
        super(ELEMENT_TYPE_STATIC_SECTION, data);
    }

    public StaticFieldsSection(int sectionType,List<StaticItem> data) {
        super(sectionType, data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected StaticFieldsSection(Parcel in) {
        super(in);
    }

    public static final Creator<StaticFieldsSection> CREATOR = new Creator<StaticFieldsSection>() {
        @Override
        public StaticFieldsSection createFromParcel(Parcel source) {
            return new StaticFieldsSection(source);
        }

        @Override
        public StaticFieldsSection[] newArray(int size) {
            return new StaticFieldsSection[size];
        }
    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TYPE_DESCRIPTION:
                v = inflater.inflate(R.layout.description_item, parent, false);
                viewHolder = new DescriptionViewHolder(v);
                break;
            case TYPE_TITLE:
                v = inflater.inflate(R.layout.title_item, parent, false);
                viewHolder = new TitleViewHolder(v);
                break;
            case TYPE_TWO_LINE_WITH_AVATAR:
                v = inflater.inflate(R.layout.avatar_with_two_lines, parent, false);
                viewHolder = new AvatarTwoLineTextViewHolder(v);
                break;
            case TYPE_DIVIDER:
                v = inflater.inflate(R.layout.divider, parent, false);
                viewHolder = new DividerViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, RecyclerView.Adapter adapter) {
        Serializable value = data.get(position).getValue();
        switch (holder.getItemViewType()) {
            case TYPE_DESCRIPTION:
                ((DescriptionViewHolder) holder).bind((String) value);
                break;
            case TYPE_TITLE:
                ((TitleViewHolder) holder).bind((CharSequence) value);
                break;
            case TYPE_TWO_LINE_WITH_AVATAR:
                ((AvatarTwoLineTextViewHolder) holder).bind((AvatarWithTwoLineTextModel) value);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }
}
