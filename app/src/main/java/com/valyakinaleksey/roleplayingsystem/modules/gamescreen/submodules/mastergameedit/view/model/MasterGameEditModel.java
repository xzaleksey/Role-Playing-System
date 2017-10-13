package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;

public class MasterGameEditModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable {

  private GameModel gameModel;
  private ArrayList<InfoSection> infoSections;

  public MasterGameEditModel() {
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeSerializable(this.infoSections);
  }

  protected MasterGameEditModel(Parcel in) {
    super(in);
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.infoSections = (ArrayList<InfoSection>) in.readSerializable();
  }

  public static final Creator<MasterGameEditModel> CREATOR = new Creator<MasterGameEditModel>() {
    @Override public MasterGameEditModel createFromParcel(Parcel source) {
      return new MasterGameEditModel(source);
    }

    @Override public MasterGameEditModel[] newArray(int size) {
      return new MasterGameEditModel[size];
    }
  };
}
