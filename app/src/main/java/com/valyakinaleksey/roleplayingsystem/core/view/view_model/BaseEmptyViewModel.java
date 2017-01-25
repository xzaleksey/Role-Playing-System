package com.valyakinaleksey.roleplayingsystem.core.view.view_model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public abstract class BaseEmptyViewModel implements EmptyViewModel, Serializable, Parcelable {

  private boolean empty;

  @Override public boolean isEmpty() {
    return empty;
  }

  @Override public void setEmpty(boolean empty) {
    this.empty = empty;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.empty ? (byte) 1 : (byte) 0);
  }

  public BaseEmptyViewModel() {
  }

  protected BaseEmptyViewModel(Parcel in) {
    this.empty = in.readByte() != 0;
  }
}
      