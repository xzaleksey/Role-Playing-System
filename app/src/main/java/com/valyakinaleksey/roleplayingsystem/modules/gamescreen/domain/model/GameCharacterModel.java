package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import java.io.Serializable;

public class GameCharacterModel
    implements Serializable, Parcelable, HasId, HasName, HasDescription {

  private String id;
  private String description;
  private String name;
  private String uid;

  @Override public String getId() {
    return id;
  }

  @Override public void setId(String id) {
    this.id = id;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  @Override public String getDescription() {
    return description;
  }

  @Override public void setDescription(String description) {
    this.description = description;
  }

  @Override public String getName() {
    return name;
  }

  @Override public void setName(String name) {
    this.name = name;
  }

  public GameCharacterModel() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.description);
    dest.writeString(this.name);
    dest.writeString(this.uid);
  }

  protected GameCharacterModel(Parcel in) {
    this.id = in.readString();
    this.description = in.readString();
    this.name = in.readString();
    this.uid = in.readString();
  }

  public static final Creator<GameCharacterModel> CREATOR = new Creator<GameCharacterModel>() {
    @Override public GameCharacterModel createFromParcel(Parcel source) {
      return new GameCharacterModel(source);
    }

    @Override public GameCharacterModel[] newArray(int size) {
      return new GameCharacterModel[size];
    }
  };
}
      