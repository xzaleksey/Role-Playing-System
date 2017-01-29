package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StorageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.io.File;
import java.io.Serializable;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.DATE_CREATE;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_MAPS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.IN_PROGRESS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class MapModel implements Serializable, Parcelable {
  public static final String MAP_MODEL_ID = "map_model_id";

  private String id;
  private String gameId;
  private String fileName;

  private boolean visible;
  private int status = IN_PROGRESS;
  private Object dateCreate;
  private Long tempDateCreate;

  @Exclude public long getDateCreateLong() {
    if (dateCreate == null || dateCreate == ServerValue.TIMESTAMP) {
      return tempDateCreate;
    }
    return (long) dateCreate;
  }

  @Exclude public String getId() {
    return id;
  }

  @Exclude public void setId(String id) {
    this.id = id;
  }

  @Exclude public String getGameId() {
    return gameId;
  }

  @Exclude public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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

  @Exclude public Observable<Uri> getDownloadUrlObservable() {
    return RxFirebaseStorage.getDownloadUrl(FirebaseStorage.getInstance()
        .getReference()
        .child(GAME_MAPS)
        .child(gameId)
        .child(id)
        .child(fileName));
  }

  @Exclude public File getLocalFile() {
    return new File(StorageUtils.getCacheDirectory()
        .concat(StringUtils.formatWithSlashes(FireBaseUtils.GAME_MAPS))
        .concat(gameId)
        .concat("/")
        .concat(id)
        .concat("/")
        .concat(fileName));
  }

  /**
   * Default constructor for mapping
   */
  public MapModel() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.fileName);
    dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
    dest.writeSerializable((Serializable) this.dateCreate);
    dest.writeValue(this.tempDateCreate);
    dest.writeInt(this.status);
    dest.writeString(this.gameId);
  }

  protected MapModel(Parcel in) {
    this.fileName = in.readString();
    this.visible = in.readByte() != 0;
    this.dateCreate = in.readSerializable();
    this.status = in.readInt();
    this.gameId = in.readString();
  }

  public static final Creator<MapModel> CREATOR = new Creator<MapModel>() {
    @Override public MapModel createFromParcel(Parcel source) {
      return new MapModel(source);
    }

    @Override public MapModel[] newArray(int size) {
      return new MapModel[size];
    }
  };

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MapModel)) return false;

    MapModel mapModel = (MapModel) o;

    return getId() != null ? getId().equals(mapModel.getId()) : mapModel.getId() == null;
  }

  @Override public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }
}
      