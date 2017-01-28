package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ServerValue;
import java.io.Serializable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.DATE_CREATE;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class MapModel implements Serializable, Parcelable {

  private String id;
  private String fileName;
  private boolean visible;
  @PropertyName(DATE_CREATE) private Object dateCreate;
  @PropertyName(TEMP_DATE_CREATE) private Long tempDateCreate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Exclude public long getDateCreateLong() {
    if (dateCreate == null || dateCreate == ServerValue.TIMESTAMP) {
      return tempDateCreate;
    }
    return (long) dateCreate;
  }

  public void setTempDateCreate(long dateCreate) {
    this.tempDateCreate = dateCreate;
  }

  public Long getTempDateCreate() {
    return tempDateCreate;
  }

  public Object getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(Object dateCreate) {
    this.dateCreate = dateCreate;
  }

  public MapModel(String fileName) {
    this.fileName = fileName;
    dateCreate = ServerValue.TIMESTAMP;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public void setTempDateCreate(Long tempDateCreate) {
    this.tempDateCreate = tempDateCreate;
  }

  public MapModel() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.fileName);
    dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
    dest.writeSerializable((Serializable) this.dateCreate);
    dest.writeValue(this.tempDateCreate);
  }

  protected MapModel(Parcel in) {
    this.id = in.readString();
    this.fileName = in.readString();
    this.visible = in.readByte() != 0;
    this.dateCreate = in.readSerializable();
    this.tempDateCreate = (Long) in.readValue(Long.class.getClassLoader());
  }

  public static final Creator<MapModel> CREATOR = new Creator<MapModel>() {
    @Override public MapModel createFromParcel(Parcel source) {
      return new MapModel(source);
    }

    @Override public MapModel[] newArray(int size) {
      return new MapModel[size];
    }
  };
}
      