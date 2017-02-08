package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.Serializable;

public abstract class AbstractGameCharacterListItem<T extends RecyclerView.ViewHolder>
    implements Serializable, Parcelable, IFlexible<T> {
  private GameCharacterModel gameCharacterModel;
  private GameClassModel gameClassModel;
  private GameRaceModel gameRaceModel;
  private boolean isMaster;
  private transient GamesCharactersPresenter gamesCharactersPresenter;

  public GamesCharactersPresenter getGamesCharactersPresenter() {
    return gamesCharactersPresenter;
  }

  public void setGamesCharactersPresenter(GamesCharactersPresenter gamesCharactersPresenter) {
    this.gamesCharactersPresenter = gamesCharactersPresenter;
  }

  public GameCharacterModel getGameCharacterModel() {
    return gameCharacterModel;
  }

  public void setGameCharacterModel(GameCharacterModel gameCharacterModel) {
    this.gameCharacterModel = gameCharacterModel;
  }

  public GameClassModel getGameClassModel() {
    return gameClassModel;
  }

  public void setGameClassModel(GameClassModel gameClassModel) {
    this.gameClassModel = gameClassModel;
  }

  public boolean isMaster() {
    return isMaster;
  }

  public void setMaster(boolean master) {
    isMaster = master;
  }

  public GameRaceModel getGameRaceModel() {
    return gameRaceModel;
  }

  public void setGameRaceModel(GameRaceModel gameRaceModel) {
    this.gameRaceModel = gameRaceModel;
  }

  public AbstractGameCharacterListItem() {
  }

  @Override public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj instanceof AbstractGameCharacterListItem) {
      AbstractGameCharacterListItem abstractGameCharacterListItem =
          (AbstractGameCharacterListItem) obj;
      GameCharacterModel gameCharacterModel = getGameCharacterModel();
      GameCharacterModel thatCharModel = abstractGameCharacterListItem.getGameCharacterModel();
      return gameCharacterModel.equals(thatCharModel) && gameCharacterModel.getName()
          .equals(thatCharModel.getName()) && isEquealsUid(gameCharacterModel, thatCharModel);
    }
    return false;
  }

  private boolean isEquealsUid(GameCharacterModel gameCharacterModel,
      GameCharacterModel thatCharModel) {

    String uid = gameCharacterModel.getUid();
    if (uid == null) {
      uid = "";
    }
    return uid.equals(thatCharModel.getUid());
  }

  @Override public int hashCode() {
    return getGameCharacterModel().hashCode();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.gameCharacterModel, flags);
    dest.writeParcelable(this.gameClassModel, flags);
    dest.writeParcelable(this.gameRaceModel, flags);
    dest.writeByte(this.isMaster ? (byte) 1 : (byte) 0);
  }

  protected AbstractGameCharacterListItem(Parcel in) {
    this.gameCharacterModel = in.readParcelable(GameCharacterModel.class.getClassLoader());
    this.gameClassModel = in.readParcelable(GameClassModel.class.getClassLoader());
    this.gameRaceModel = in.readParcelable(GameRaceModel.class.getClassLoader());
    this.isMaster = in.readByte() != 0;
  }
}
      