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
  private String classId;
  private String raceId;

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

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getRaceId() {
    return raceId;
  }

  public void setRaceId(String raceId) {
    this.raceId = raceId;
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
    dest.writeString(this.classId);
    dest.writeString(this.raceId);
  }

  protected GameCharacterModel(Parcel in) {
    this.id = in.readString();
    this.description = in.readString();
    this.name = in.readString();
    this.uid = in.readString();
    this.classId = in.readString();
    this.raceId = in.readString();
  }

  public static final Creator<GameCharacterModel> CREATOR = new Creator<GameCharacterModel>() {
    @Override public GameCharacterModel createFromParcel(Parcel source) {
      return new GameCharacterModel(source);
    }

    @Override public GameCharacterModel[] newArray(int size) {
      return new GameCharacterModel[size];
    }
  };

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameCharacterModel)) return false;

    GameCharacterModel that = (GameCharacterModel) o;

    return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
  }

  @Override public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }
}
      