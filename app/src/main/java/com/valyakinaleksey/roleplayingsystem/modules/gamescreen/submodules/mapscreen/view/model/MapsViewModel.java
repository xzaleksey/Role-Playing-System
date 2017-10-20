package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.io.Serializable;

public class MapsViewModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable {

  private GameModel gameModel;
  private transient MapsListFlexibleModel mapModel;
  private boolean isMaster;

  public MapsViewModel() {
  }

  public MapsListFlexibleModel getMapModel() {
    return mapModel;
  }

  public void setMapModel(MapsListFlexibleModel mapModel) {
    this.mapModel = mapModel;
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  public void setGameModel(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  @Override public boolean isUpdatedRequired() {
    return mapModel == null;
  }

  public boolean isMaster() {
    return isMaster;
  }

  public void setMaster(boolean master) {
    isMaster = master;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeByte(this.isMaster ? (byte) 1 : (byte) 0);
  }

  protected MapsViewModel(Parcel in) {
    super(in);
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.isMaster = in.readByte() != 0;
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
