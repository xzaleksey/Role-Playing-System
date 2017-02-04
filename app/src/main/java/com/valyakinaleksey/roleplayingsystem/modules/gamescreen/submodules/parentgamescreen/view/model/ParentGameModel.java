package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.utils.SerializebleTuple;
import com.valyakinaleksey.roleplayingsystem.core.utils.Tuple;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseEmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParentGameModel extends BaseEmptyViewModel {
  public static final String USER_SCREEN = "user_screen";
  public static final String MASTER_SCREEN = "master_screen";

  private GameModel gameModel;
  private boolean isMaster;
  private String navigationTag;
  private boolean firstNavigation = true;
  private ArrayList<SerializebleTuple<Integer, String>> fragmentsInfo;

  public ParentGameModel() {
    fragmentsInfo = new ArrayList<>();
  }

  public boolean isMaster() {
    return isMaster;
  }

  public void setMaster(boolean master) {
    isMaster = master;
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  public void setGameModel(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  public boolean isFirstNavigation() {
    return firstNavigation;
  }

  public void setFirstNavigation(boolean firstNavigation) {
    this.firstNavigation = firstNavigation;
  }

  @Override public boolean isEmpty() {
    return firstNavigation;
  }

  public void setNavigationTag(String navigationTag) {
    this.navigationTag = navigationTag;
  }

  public String getNavigationTag() {
    return navigationTag;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeByte(this.isMaster ? (byte) 1 : (byte) 0);
    dest.writeString(this.navigationTag);
    dest.writeByte(this.firstNavigation ? (byte) 1 : (byte) 0);
    dest.writeSerializable(this.fragmentsInfo);
  }

  public ArrayList<SerializebleTuple<Integer, String>> getFragmentsInfo() {
    return fragmentsInfo;
  }

  public void setFragmentsInfo(ArrayList<SerializebleTuple<Integer, String>> fragmentsInfo) {
    this.fragmentsInfo = fragmentsInfo;
  }

  @SuppressWarnings("unchecked") protected ParentGameModel(Parcel in) {
    super(in);
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.isMaster = in.readByte() != 0;
    this.navigationTag = in.readString();
    this.firstNavigation = in.readByte() != 0;
    this.fragmentsInfo = (ArrayList<SerializebleTuple<Integer, String>>) in.readSerializable();
  }

  public static final Creator<ParentGameModel> CREATOR = new Creator<ParentGameModel>() {
    @Override public ParentGameModel createFromParcel(Parcel source) {
      return new ParentGameModel(source);
    }

    @Override public ParentGameModel[] newArray(int size) {
      return new ParentGameModel[size];
    }
  };
}
