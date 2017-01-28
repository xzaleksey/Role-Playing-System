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
import java.io.Serializable;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.DATE_CREATE;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_MAPS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TEMP_DATE_CREATE;

public class MapModel implements Serializable, Parcelable {
  public static final int IN_PROGRESS = 0;
  public static final int ERROR = -1;
  public static final int SUCCESS = 1;
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
}
      