package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;

import android.support.v7.widget.RecyclerView;
import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import java.io.Serializable;
import java.util.List;

public abstract class ExpandableSection<T extends Serializable> extends InfoSectionImpl<T> {
  protected boolean expanded = false;

  public ExpandableSection(int sectionType, String title, List<T> data) {
    super(sectionType, title, data);
  }

  public boolean isExpanded() {
    return expanded;
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
  }

  @Override public void update(RecyclerView.Adapter sectionsAdapter, int sectionPosition,
      DataEvent dataEvent) {
    if (expanded) {
      super.update(sectionsAdapter, sectionPosition, dataEvent);
    } else {
      updateTitle(sectionsAdapter, sectionPosition);
    }
  }

  protected ExpandableSection(Parcel in) {
    super(in);
    this.expanded = in.readByte() != 0;
  }

  @Override public int getItemCount() {
    return expanded ? super.getItemCount() : 1;
  }
}
      