package com.valyakinaleksey.roleplayingsystem.core.view.view_model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public abstract class BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Serializable, Parcelable {

  private boolean empty = true;
  private boolean restored;

  @Override public boolean isUpdatedRequired() {
    return empty;
  }

  @Override public void setNeedUpdate(boolean empty) {
    this.empty = empty;
  }

  public BaseRequestUpdateViewModel() {
  }

  @Override public void setRestored(boolean restored) {
    this.restored = restored;
  }

  @Override public boolean isRestored() {
    return restored;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.empty ? (byte) 1 : (byte) 0);
    dest.writeByte(this.restored ? (byte) 1 : (byte) 0);
  }

  protected BaseRequestUpdateViewModel(Parcel in) {
    this.empty = in.readByte() != 0;
    this.restored = in.readByte() != 0;
  }

  @Override public String toString() {
    return "{empty=" + empty + ", restored=" + restored + '}';
  }
}
      