package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseEmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;

public class GamesCharactersViewModel extends BaseEmptyViewModel
    implements EmptyViewModel, Parcelable, Serializable {

  private String toolbarTitle;
  private GameModel gameModel;
  private ArrayList<InfoSection> infoSections;

  public GamesCharactersViewModel() {
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

  @Override public boolean isEmpty() {
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

  protected GamesCharactersViewModel(Parcel in) {
    super(in);
    this.toolbarTitle = in.readString();
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.infoSections = (ArrayList<InfoSection>) in.readSerializable();
  }

  public static final Creator<GamesCharactersViewModel> CREATOR = new Creator<GamesCharactersViewModel>() {
    @Override public GamesCharactersViewModel createFromParcel(Parcel source) {
      return new GamesCharactersViewModel(source);
    }

    @Override public GamesCharactersViewModel[] newArray(int size) {
      return new GamesCharactersViewModel[size];
    }
  };
}
