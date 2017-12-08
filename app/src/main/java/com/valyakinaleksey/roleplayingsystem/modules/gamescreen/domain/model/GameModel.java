package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;

import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.FIELD_NAME;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.ID;

public class GameModel implements Serializable, Parcelable, HasId {
  public static final String FIELD_FINISHED = "finished";
  public static final String FIELD_MASTER_ID = "masterId";
  public static final String FIELD_MASTER_NAME = "masterName";
  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_DATE_CREATE = "dateCreate";
  public static final String FIELD_TEMP_DATE_CREATE = "tempDateCreate";

  public static final String KEY = GameModel.class.getSimpleName();
  private String id;
  private String masterId;
  private String masterName = StringUtils.UNDEFINED;
  private String name = StringUtils.EMPTY_STRING;
  private String description = StringUtils.EMPTY_STRING;
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

  @Exclude public Map<String, Object> toGameInUserMap() {
    HashMap<String, Object> result = new LinkedHashMap<>();
    result.put(GameInUserModel.FIELD_LAST_VISITED_DATE, ServerValue.TIMESTAMP);
    return result;
  }

  @Exclude public Map<String, Object> toGameModelMap() {
    HashMap<String, Object> result = new LinkedHashMap<>();
    result.put(ID, id);
    result.put(FIELD_NAME, name);
    result.put(FIELD_MASTER_ID, masterId);
    result.put(FIELD_MASTER_NAME, masterName);
    result.put(FIELD_PASSWORD, password);
    result.put(FIELD_DATE_CREATE, dateCreate);
    result.put(FIELD_TEMP_DATE_CREATE, tempDateCreate);
    result.put(FIELD_FINISHED, finished);
    return result;
  }

  public String getMasterName() {
    return masterName;
  }

  public void setMasterName(String masterName) {
    this.masterName = masterName;
  }

  @Override public String getId() {
    return id;
  }

  @Override public void setId(String id) {
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

  @Exclude public boolean isMaster(String guid) {
    return masterId != null ? masterId.equals(guid) : guid == null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GameModel gameModel = (GameModel) o;

    if (finished != gameModel.finished) return false;
    if (id != null ? !id.equals(gameModel.id) : gameModel.id != null) return false;
    if (masterId != null ? !masterId.equals(gameModel.masterId) : gameModel.masterId != null) {
      return false;
    }
    if (masterName != null ? !masterName.equals(gameModel.masterName)
        : gameModel.masterName != null) {
      return false;
    }
    if (name != null ? !name.equals(gameModel.name) : gameModel.name != null) return false;
    if (description != null ? !description.equals(gameModel.description)
        : gameModel.description != null) {
      return false;
    }
    if (password != null ? !password.equals(gameModel.password) : gameModel.password != null) {
      return false;
    }
    if (dateCreate != null ? !dateCreate.equals(gameModel.dateCreate)
        : gameModel.dateCreate != null) {
      return false;
    }
    return true;
  }

  @Override public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (masterId != null ? masterId.hashCode() : 0);
    result = 31 * result + (masterName != null ? masterName.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (dateCreate != null ? dateCreate.hashCode() : 0);
    result = 31 * result + (tempDateCreate != null ? tempDateCreate.hashCode() : 0);
    result = 31 * result + (finished ? 1 : 0);
    return result;
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
      