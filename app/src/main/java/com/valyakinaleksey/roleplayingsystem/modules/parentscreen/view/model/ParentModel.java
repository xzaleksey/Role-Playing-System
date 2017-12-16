package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model;

import android.os.Parcel;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;

public class ParentModel extends BaseRequestUpdateViewModel {

  private int navigationId;
  private boolean firstNavigation = true;
  private boolean isDisconnected;

  public int getNavigationId() {
    return navigationId;
  }

  public void setNavigationId(int navigationId) {
    this.navigationId = navigationId;
  }

  public boolean isFirstNavigation() {
    return firstNavigation;
  }

  public void setFirstNavigation(boolean firstNavigation) {
    this.firstNavigation = firstNavigation;
  }

  public boolean isDisconnected() {
    return isDisconnected;
  }

  public void setDisconnected(boolean disconnected) {
    isDisconnected = disconnected;
  }

  public ParentModel() {
  }

  @Override public boolean isUpdatedRequired() {
    return firstNavigation;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeInt(this.navigationId);
    dest.writeByte(this.firstNavigation ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isDisconnected ? (byte) 1 : (byte) 0);
  }

  protected ParentModel(Parcel in) {
    super(in);
    this.navigationId = in.readInt();
    this.firstNavigation = in.readByte() != 0;
    this.isDisconnected = in.readByte() != 0;
  }

  public static final Creator<ParentModel> CREATOR = new Creator<ParentModel>() {
    @Override public ParentModel createFromParcel(Parcel source) {
      return new ParentModel(source);
    }

    @Override public ParentModel[] newArray(int size) {
      return new ParentModel[size];
    }
  };
}
