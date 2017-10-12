package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import com.google.firebase.database.ServerValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.DATE_CREATE;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.FIELD_NAME;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class GameModel implements Serializable, Parcelable {
  public static final String FIELD_FINISHED = "finished";
  public static final String FIELD_MASTER_ID = "masterId";

  public static final String KEY = GameModel.class.getSimpleName();
  private String id;
  private String masterId;
  private String masterName;
  private String name;
  private String description;
  private String password;
  private Object dateCreate;
  private Long tempDateCreate;
  private boolean finished;

  /**
   * Don' use default constructor, it is for firebase and parcelable
   */
  public GameModel() {

  }

  public GameModel(String name, String description) {
    this();
    this.dateCreate = ServerValue.TIMESTAMP;
    this.name = name;
    this.description = description;
  }

  public GameModel(String masterId, String name, long dateCreate, String description) {
    this(name, description);
    this.masterId = masterId;
    this.dateCreate = dateCreate;
  }

  public GameModel(String id, String masterId, String name, long dateCreate, String description) {
    this(masterId, name, dateCreate, description);
    this.id = id;
  }

  @Exclude public Map<String, Object> toMap() {
    HashMap<String, Object> result = new LinkedHashMap<>();
    result.put(ID, id);
    result.put(FIELD_NAME, name);
    return result;
  }

  public String getMasterName() {
    return masterName;
  }

  public void setMasterName(String masterName) {
    this.masterName = masterName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMasterId() {
    return masterId;
  }

  public void setMasterId(String masterId) {
    this.masterId = masterId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Exclude public long getDateCreateLong() {
    if (dateCreate == null || dateCreate == ServerValue.TIMESTAMP) {
      return tempDateCreate;
    }
    return (long) dateCreate;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
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
    dest.writeString(this.masterId);
    dest.writeString(this.masterName);
    dest.writeString(this.name);
    dest.writeString(this.description);
    dest.writeString(this.password);
    dest.writeSerializable((Serializable) this.dateCreate);
    dest.writeValue(this.tempDateCreate);
  }

  protected GameModel(Parcel in) {
    this.id = in.readString();
    this.masterId = in.readString();
    this.masterName = in.readString();
    this.name = in.readString();
    this.description = in.readString();
    this.password = in.readString();
    this.dateCreate = in.readSerializable();
    this.tempDateCreate = (Long) in.readValue(Long.class.getClassLoader());
  }

  public static final Creator<GameModel> CREATOR = new Creator<GameModel>() {
    @Override public GameModel createFromParcel(Parcel source) {
      return new GameModel(source);
    }

    @Override public GameModel[] newArray(int size) {
      return new GameModel[size];
    }
  };
}
      