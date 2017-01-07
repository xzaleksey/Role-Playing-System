package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.adapter.DescriptionViewHolder;

import java.io.Serializable;
import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_STATIC_SECTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;

public class StaticFieldsSection extends InfoSectionImpl<StaticItem> {

    public StaticFieldsSection(List<StaticItem> data) {
        super(ELEMENT_TYPE_STATIC_SECTION, data);
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
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Serializable value = data.get(position).getValue();
        switch (holder.getItemViewType()) {
            case TYPE_DESCRIPTION:
                ((DescriptionViewHolder) holder).bind((String) value);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }
}
