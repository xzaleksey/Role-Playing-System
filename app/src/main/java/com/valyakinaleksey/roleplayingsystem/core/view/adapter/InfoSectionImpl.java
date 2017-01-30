package com.valyakinaleksey.roleplayingsystem.core.view.adapter;

import android.os.Parcel;

import android.support.v7.widget.RecyclerView;
import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoSectionImpl<T extends Serializable> implements InfoSection<T> {
  private int sectionType;
  private boolean active = true;
  protected ArrayList<T> data;
  private String title;

  public InfoSectionImpl(int sectionType, String title, List<T> data) {
    this(sectionType, data);
    this.title = title;
  }

  public InfoSectionImpl(int sectionType, List<T> data) {
    this.sectionType = sectionType;
    this.data = new ArrayList<>(data);
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override public int getSectionType() {
    return sectionType;
  }

  @Override public boolean isActive() {
    return active;
  }

  @Override public void setActive(boolean active) {
    this.active = active;
  }

  @Override public ArrayList<T> getData() {
    return data;
  }

  @Override public void setData(ArrayList<T> data) {
    this.data = data;
  }

  @Override public String getTitle() {
    return title;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void update(RecyclerView.Adapter sectionsAdapter, int sectionPosition,
      DataEvent dataEvent) {
    sectionPosition = updateTitle(sectionsAdapter, sectionPosition);
    switch (dataEvent.getType()) {
      case ADDED:
        sectionsAdapter.notifyItemInserted(sectionPosition + dataEvent.getIndex());
        break;
      case REMOVED:
        sectionsAdapter.notifyItemRemoved(sectionPosition + dataEvent.getIndex());
        break;
      case CHANGED:
        sectionsAdapter.notifyItemChanged(sectionPosition + dataEvent.getIndex());
        break;
      case MOVED:
        sectionsAdapter.notifyItemMoved(sectionPosition + dataEvent.getOldIndex(),
            sectionPosition + dataEvent.getIndex());
        break;
    }
  }

  protected int updateTitle(RecyclerView.Adapter sectionsAdapter, int sectionPosition) {
    if (getTitle() != null) {
      sectionsAdapter.notifyItemChanged(sectionPosition);
      sectionPosition++;
    }
    return sectionPosition;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.sectionType);
    dest.writeByte(this.active ? (byte) 1 : (byte) 0);
    dest.writeSerializable(this.data);
    dest.writeString(this.title);
  }

  @SuppressWarnings("unchecked") protected InfoSectionImpl(Parcel in) {
    this.sectionType = in.readInt();
    this.active = in.readByte() != 0;
    this.data = (ArrayList<T>) in.readSerializable();
    this.title = in.readString();
  }

  @Override public int getItemCount() {
    return data.size() + (title != null ? 1 : 0);
  }
}
