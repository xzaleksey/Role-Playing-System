package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.io.Serializable;
import java.util.ArrayList;

public class CopyViewModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable {

  private String toolbarTitle;
  private GameModel gameModel;
  private ArrayList<InfoSection> infoSections;

  public CopyViewModel() {
  }

  public String getToolbarTitle() {
    return toolbarTitle;
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  public void setGameModel(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  public ArrayList<InfoSection> getInfoSections() {
    return infoSections;
  }

  public void setInfoSections(ArrayList<InfoSection> infoSections) {
    this.infoSections = infoSections;
  }

  public void setToolbarTitle(String toolbarTitle) {
    this.toolbarTitle = toolbarTitle;
  }

  @Override public boolean isUpdatedRequired() {
    return false;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.toolbarTitle);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeSerializable(this.infoSections);
  }

  protected CopyViewModel(Parcel in) {
    super(in);
    this.toolbarTitle = in.readString();
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.infoSections = (ArrayList<InfoSection>) in.readSerializable();
  }

  public static final Creator<CopyViewModel> CREATOR = new Creator<CopyViewModel>() {
    @Override public CopyViewModel createFromParcel(Parcel source) {
      return new CopyViewModel(source);
    }

    @Override public CopyViewModel[] newArray(int size) {
      return new CopyViewModel[size];
    }
  };
}
