package com.valyakinaleksey.roleplayingsystem.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class DataEvent implements Serializable, Parcelable {
  public enum EventType {ADDED, CHANGED, REMOVED, MOVED}

  private EventType type;
  private int index;
  private int oldIndex;

  public DataEvent(EventType type, int index) {
    this.type = type;
    this.index = index;
  }

  public EventType getType() {
    return type;
  }

  public void setType(EventType type) {
    this.type = type;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getOldIndex() {
    return oldIndex;
  }

  public void setOldIndex(int oldIndex) {
    this.oldIndex = oldIndex;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    dest.writeInt(this.index);
    dest.writeInt(this.oldIndex);
  }

  protected DataEvent(Parcel in) {
    int tmpType = in.readInt();
    this.type = tmpType == -1 ? null : EventType.values()[tmpType];
    this.index = in.readInt();
    this.oldIndex = in.readInt();
  }

  public static final Creator<DataEvent> CREATOR = new Creator<DataEvent>() {
    @Override public DataEvent createFromParcel(Parcel source) {
      return new DataEvent(source);
    }

    @Override public DataEvent[] newArray(int size) {
      return new DataEvent[size];
    }
  };
}
      