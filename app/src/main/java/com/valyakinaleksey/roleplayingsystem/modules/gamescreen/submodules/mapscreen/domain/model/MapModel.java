package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.kelvinapps.rxfirebase.RxFirebaseStorage;
import com.valyakinaleksey.roleplayingsystem.utils.FirebaseTable;
import com.valyakinaleksey.roleplayingsystem.utils.StorageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import rx.Observable;

import java.io.File;
import java.io.Serializable;

import static com.valyakinaleksey.roleplayingsystem.utils.FirebaseTable.GAME_MAPS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.IN_PROGRESS;

public class MapModel implements Serializable, Parcelable {
    public static final String MAP_MODEL_ID = "map_model_id";
    public static final String PHOTO_URL = "photoUrl";

    private String id;
    private String gameId;
    private String fileName;
    private boolean visible;
    private int status = IN_PROGRESS;
    private Object dateCreate;
    private Long tempDateCreate;
    public String photoUrl = StringUtils.EMPTY_STRING;

    /**
     * Default constructor for mapping
     */
    public MapModel() {
    }

    @Exclude
    public long getDateCreateLong() {
        if (dateCreate == null || dateCreate == ServerValue.TIMESTAMP) {
            return tempDateCreate;
        }
        return (long) dateCreate;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getGameId() {
        return gameId;
    }

    @Exclude
    public void setGameId(String gameId) {
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

    @NonNull
    private Observable<Uri> getDownloadObservable() {
        return RxFirebaseStorage.getDownloadUrl(FirebaseStorage.getInstance()
                .getReference()
                .child(GAME_MAPS)
                .child(gameId)
                .child(id)
                .child(fileName));
    }

    @Exclude
    public File getLocalFile() {
        return new File(StorageUtils.getImagesCacheDirectory()
                .concat(StringUtils.formatWithSlashes(FirebaseTable.GAME_MAPS))
                .concat(gameId)
                .concat("/")
                .concat(id)
                .concat("/")
                .concat(fileName));
    }

    @Exclude
    public boolean localFileExists() {
        return getLocalFile().exists();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapModel)) return false;

        MapModel mapModel = (MapModel) o;

        return getId() != null ? getId().equals(mapModel.getId()) : mapModel.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.gameId);
        dest.writeString(this.fileName);
        dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
        dest.writeInt(this.status);
        dest.writeValue(this.tempDateCreate);
        dest.writeString(this.photoUrl);
    }

    protected MapModel(Parcel in) {
        this.id = in.readString();
        this.gameId = in.readString();
        this.fileName = in.readString();
        this.visible = in.readByte() != 0;
        this.status = in.readInt();
        this.tempDateCreate = (Long) in.readValue(Long.class.getClassLoader());
        this.photoUrl = in.readString();
    }

    public static final Creator<MapModel> CREATOR = new Creator<MapModel>() {
        @Override
        public MapModel createFromParcel(Parcel source) {
            return new MapModel(source);
        }

        @Override
        public MapModel[] newArray(int size) {
            return new MapModel[size];
        }
    };
}
      