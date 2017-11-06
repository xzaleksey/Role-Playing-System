package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.io.Serializable;

public class GameClassModel implements Serializable, Parcelable, HasId, HasName, HasDescription {
  private String id = StringUtils.UNDEFINED;
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

  public GameClassModel() {
  }

  protected GameClassModel(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.description = in.readString();
  }

  public static final Creator<GameClassModel> CREATOR = new Creator<GameClassModel>() {
    @Override public GameClassModel createFromParcel(Parcel source) {
      return new GameClassModel(source);
    }

    @Override public GameClassModel[] newArray(int size) {
      return new GameClassModel[size];
    }
  };

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameClassModel)) return false;

    GameClassModel that = (GameClassModel) o;

    return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
  }

  @Override public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

}
      