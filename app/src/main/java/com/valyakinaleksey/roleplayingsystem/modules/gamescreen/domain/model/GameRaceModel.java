package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.io.Serializable;

public class GameRaceModel implements Serializable, Parcelable, HasId, HasName, HasDescription {
  private String id = "UNDEFINED";

  private String name = StringUtils.EMPTY_STRING;
  private String description = StringUtils.EMPTY_STRING;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.description);
  }

  public GameRaceModel() {
  }

  protected GameRaceModel(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.description = in.readString();
  }

  public static final Creator<GameRaceModel> CREATOR = new Creator<GameRaceModel>() {
    @Override public GameRaceModel createFromParcel(Parcel source) {
      return new GameRaceModel(source);
    }

    @Override public GameRaceModel[] newArray(int size) {
      return new GameRaceModel[size];
    }
  };

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameRaceModel)) return false;

    GameRaceModel that = (GameRaceModel) o;

    return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
  }

  @Override public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }
}
      