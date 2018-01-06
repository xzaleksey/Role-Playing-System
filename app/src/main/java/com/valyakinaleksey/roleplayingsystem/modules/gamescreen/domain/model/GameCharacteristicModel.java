package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;

import java.io.Serializable;

public class GameCharacteristicModel
    implements Serializable, Parcelable, HasId, HasName, HasDescription {
  private String id;

  private String name;
  private String description;

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

  public GameCharacteristicModel() {
  }

  protected GameCharacteristicModel(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.description = in.readString();
  }

  public static final Creator<GameCharacteristicModel> CREATOR =
      new Creator<GameCharacteristicModel>() {
        @Override public GameCharacteristicModel createFromParcel(Parcel source) {
          return new GameCharacteristicModel(source);
        }

        @Override public GameCharacteristicModel[] newArray(int size) {
          return new GameCharacteristicModel[size];
        }
      };
}
      