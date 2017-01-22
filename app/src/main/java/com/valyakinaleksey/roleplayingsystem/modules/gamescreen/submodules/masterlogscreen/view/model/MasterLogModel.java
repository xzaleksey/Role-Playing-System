package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;

public class MasterLogModel implements EmptyViewModel, Parcelable, Serializable {

  private GameModel gameModel;
  private transient DatabaseReference databaseReference;

  public MasterLogModel() {
  }

  public DatabaseReference getDatabaseReference() {
    return databaseReference;
  }

  public void setDatabaseReference(DatabaseReference databaseReference) {
    this.databaseReference = databaseReference;
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  public void setGameModel(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  @Override public boolean isEmpty() {
    return databaseReference == null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.gameModel, flags);
  }

  protected MasterLogModel(Parcel in) {
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
  }

  public static final Creator<MasterLogModel> CREATOR = new Creator<MasterLogModel>() {
    @Override public MasterLogModel createFromParcel(Parcel source) {
      return new MasterLogModel(source);
    }

    @Override public MasterLogModel[] newArray(int size) {
      return new MasterLogModel[size];
    }
  };
}
