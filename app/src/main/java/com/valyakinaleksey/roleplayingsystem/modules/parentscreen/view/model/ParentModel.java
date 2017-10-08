package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model;

import android.os.Parcel;

import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseShouldRequestUpdateViewModel;

public class ParentModel extends BaseShouldRequestUpdateViewModel {

  private int navigationTag;
  private boolean firstNavigation = true;
  private boolean isDisconnected;
  private String toolbarTitle;

  public int getNavigationId() {
    return navigationTag;
  }

  public void setNavigationId(int navigationId) {
    this.navigationTag = navigationId;
  }

  public boolean isFirstNavigation() {
    return firstNavigation;
  }

  public void setFirstNavigation(boolean firstNavigation) {
    this.firstNavigation = firstNavigation;
  }

  public String getToolbarTitle() {
    return toolbarTitle;
  }

  public void setToolbarTitle(String toolbarTitle) {
    this.toolbarTitle = toolbarTitle;
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
    dest.writeInt(this.navigationTag);
    dest.writeByte(this.firstNavigation ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isDisconnected ? (byte) 1 : (byte) 0);
    dest.writeString(this.toolbarTitle);
  }

  protected ParentModel(Parcel in) {
    super(in);
    this.navigationTag = in.readInt();
    this.firstNavigation = in.readByte() != 0;
    this.isDisconnected = in.readByte() != 0;
    this.toolbarTitle = in.readString();
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
