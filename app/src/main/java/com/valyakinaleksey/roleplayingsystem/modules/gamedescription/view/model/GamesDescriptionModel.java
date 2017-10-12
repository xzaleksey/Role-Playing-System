package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GamesDescriptionModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable {

  private String toolbarTitle;
  private GameModel gameModel;
  private ArrayList<InfoSection> infoSections;

  public GamesDescriptionModel() {
    infoSections = new ArrayList<>();
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.toolbarTitle);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeSerializable(this.infoSections);
  }

  protected GamesDescriptionModel(Parcel in) {
    super(in);
    this.toolbarTitle = in.readString();
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.infoSections = (ArrayList<InfoSection>) in.readSerializable();
  }

  public static final Creator<GamesDescriptionModel> CREATOR =
      new Creator<GamesDescriptionModel>() {
        @Override public GamesDescriptionModel createFromParcel(Parcel source) {
          return new GamesDescriptionModel(source);
        }

        @Override public GamesDescriptionModel[] newArray(int size) {
          return new GamesDescriptionModel[size];
        }
      };

  @Override public String toString() {
    return super.toString()
        + " toolbarTitle='"
        + toolbarTitle
        + '\''
        + ", gameModel="
        + gameModel
        + ", infoSections="
        + infoSections
        + '}';
  }
}
