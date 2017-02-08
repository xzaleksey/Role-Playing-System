package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseEmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamesCharactersViewModel extends BaseEmptyViewModel
    implements EmptyViewModel, Parcelable, Serializable {

  private GameModel gameModel;
  private transient List<IFlexible> iFlexibles;
  private boolean master;
  private boolean hasCharacter;

  public boolean isMaster() {
    return master;
  }

  public void setMaster(boolean master) {
    this.master = master;
  }

  public GamesCharactersViewModel() {
    iFlexibles = new ArrayList<>();
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  public void setGameModel(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  public List<IFlexible> getiFlexibles() {
    return iFlexibles;
  }

  public void setiFlexibles(List<IFlexible> iFlexibles) {
    this.iFlexibles = iFlexibles;
  }

  @Override public boolean isEmpty() {
    return iFlexibles.isEmpty();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeInt(master ? 1 : 0);
    dest.writeInt(hasCharacter ? 1 : 0);
  }

  protected GamesCharactersViewModel(Parcel in) {
    super(in);
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.master = in.readInt() == 1;
    this.hasCharacter = in.readInt() == 1;
  }

  public static final Creator<GamesCharactersViewModel> CREATOR =
      new Creator<GamesCharactersViewModel>() {
        @Override public GamesCharactersViewModel createFromParcel(Parcel source) {
          return new GamesCharactersViewModel(source);
        }

        @Override public GamesCharactersViewModel[] newArray(int size) {
          return new GamesCharactersViewModel[size];
        }
      };

  public void setHasCharacter(boolean hasCharacter) {
    this.hasCharacter = hasCharacter;
  }

  public boolean hasCharacter() {
    return hasCharacter;
  }
}
