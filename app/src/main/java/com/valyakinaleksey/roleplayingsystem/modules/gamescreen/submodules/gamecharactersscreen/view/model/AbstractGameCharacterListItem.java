package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.Serializable;

public abstract class AbstractGameCharacterListItem<T extends RecyclerView.ViewHolder>
    implements Serializable, Parcelable, IFlexible<T> {
  private GameCharacterModel gameCharacterModel;
  private GameModel gameModel;
  private boolean isMaster;
  protected int type;
  private transient GamesCharactersPresenter gamesCharactersPresenter;

  public GamesCharactersPresenter getGamesCharactersPresenter() {
    return gamesCharactersPresenter;
  }

  public void setGamesCharactersPresenter(GamesCharactersPresenter gamesCharactersPresenter) {
    this.gamesCharactersPresenter = gamesCharactersPresenter;
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  public void setGameModel(GameModel gameModel) {
    this.gameModel = gameModel;
  }

  public GameCharacterModel getGameCharacterModel() {
    return gameCharacterModel;
  }

  public void setGameCharacterModel(GameCharacterModel gameCharacterModel) {
    this.gameCharacterModel = gameCharacterModel;
  }

  public boolean isMaster() {
    return isMaster;
  }

  public void setMaster(boolean master) {
    isMaster = master;
  }

  public AbstractGameCharacterListItem() {
  }

  public int getType() {
    return type;
  }

  @Override public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj instanceof AbstractGameCharacterListItem) {
      AbstractGameCharacterListItem abstractGameCharacterListItem =
          (AbstractGameCharacterListItem) obj;
      GameCharacterModel gameCharacterModel = getGameCharacterModel();
      GameCharacterModel thatCharModel = abstractGameCharacterListItem.getGameCharacterModel();
      return gameCharacterModel.equals(thatCharModel) && gameCharacterModel.getName()
          .equals(thatCharModel.getName()) && isEqualsUid(gameCharacterModel, thatCharModel);
    }
    return false;
  }

  private boolean isEqualsUid(GameCharacterModel gameCharacterModel,
      GameCharacterModel thatCharModel) {

    String uid = gameCharacterModel.getUid();
    if (uid == null) {
      uid = "";
    }
    return uid.equals(thatCharModel.getUid());
  }

  @Override public int hashCode() {
    return getGameCharacterModel().getId().hashCode();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.gameCharacterModel, flags);
    dest.writeByte(this.isMaster ? (byte) 1 : (byte) 0);
    dest.writeParcelable(this.gameModel, flags);
    dest.writeInt(type);
  }

  protected AbstractGameCharacterListItem(Parcel in) {
    this.gameCharacterModel = in.readParcelable(GameCharacterModel.class.getClassLoader());
    this.isMaster = in.readByte() != 0;
    this.gameModel = in.readParcelable(GameModel.class.getClassLoader());
    this.type = in.readInt();
  }
}
      