package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

public abstract class ExpandableSection<T extends Serializable> extends InfoSectionImpl<T> {
    private boolean expanded = false;

    public ExpandableSection(int sectionType, String title, List<T> data) {
        super(sectionType, title, data);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
    }

    protected ExpandableSection(Parcel in) {
        super(in);
        this.expanded = in.readByte() != 0;
    }

    @Override
    public int getItemCount() {
        return expanded ? super.getItemCount() : 1;
    }
}
      