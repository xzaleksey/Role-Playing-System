package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseEmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.io.Serializable;

public class MapsViewModel extends BaseEmptyViewModel
    implements EmptyViewModel, Parcelable, Serializable {

  private GameModel gameModel;
  private transient DatabaseReference databaseReference;

  public MapsViewModel() {
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
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.gameModel, flags);
  }

  protected MapsViewModel(Parcel in) {
    super(in);
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
  }

  public static final Creator<MapsViewModel> CREATOR = new Creator<MapsViewModel>() {
    @Override public MapsViewModel createFromParcel(Parcel source) {
      return new MapsViewModel(source);
    }

    @Override public MapsViewModel[] newArray(int size) {
      return new MapsViewModel[size];
    }
  };
}
