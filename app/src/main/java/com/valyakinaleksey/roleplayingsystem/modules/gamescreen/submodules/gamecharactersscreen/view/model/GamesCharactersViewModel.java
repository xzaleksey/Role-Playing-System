package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.view.customview.AnimatedTitlesLayout;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;

import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamesCharactersViewModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable {
  public static final int OCCUPIED_TAB = 0;
  public static final int FREE_TAB = 1;
  public static final int NPC_TAB = 2;

  private GameModel gameModel;
  private transient List<IFlexible> iFlexibles;
  private boolean master;
  private boolean hasCharacter;
  private int navigationTab;
  private transient List<AnimatedTitlesLayout.TitleModel> titleModels;

  public boolean isMaster() {
    return master;
  }

  public List<AnimatedTitlesLayout.TitleModel> getTitleModels() {
    return titleModels;
  }

  public void setTitleModels(List<AnimatedTitlesLayout.TitleModel> titleModels) {
    this.titleModels = titleModels;
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

  @Override public boolean isUpdatedRequired() {
    return false;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeInt(master ? 1 : 0);
    dest.writeInt(hasCharacter ? 1 : 0);
    dest.writeInt(navigationTab);
  }

  protected GamesCharactersViewModel(Parcel in) {
    super(in);
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.master = in.readInt() == 1;
    this.hasCharacter = in.readInt() == 1;
    this.navigationTab = in.readInt();
  }

  public int getNavigationTab() {
    return navigationTab;
  }

  public void setNavigationTab(int navigationTab) {
    this.navigationTab = navigationTab;
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
