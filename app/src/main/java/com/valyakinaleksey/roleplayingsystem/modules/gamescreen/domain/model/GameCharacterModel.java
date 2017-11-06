package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Exclude;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

import java.io.Serializable;

public class GameCharacterModel
        implements Serializable, Parcelable, HasId, HasName, HasDescription {
    public static final String VISIBLE = "visible";
    private String id;
    private String description;
    private String name;
    private String uid;
    private String classId;
    private String raceId;
    public String photoUrl;
    public boolean visible = true;
    @Exclude
    public User user;
    @Exclude
    public GameRaceModel gameRaceModel;
    @Exclude
    public GameClassModel gameClassModel;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCharacterModel that = (GameCharacterModel) o;

        if (visible != that.visible) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (raceId != null ? !raceId.equals(that.raceId) : that.raceId != null) return false;
        if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (gameRaceModel != null ? !gameRaceModel.equals(that.gameRaceModel)
                : that.gameRaceModel != null) {
            return false;
        }
        return gameClassModel != null ? gameClassModel.equals(that.gameClassModel)
                : that.gameClassModel == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (raceId != null ? raceId.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + (visible ? 1 : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (gameRaceModel != null ? gameRaceModel.hashCode() : 0);
        result = 31 * result + (gameClassModel != null ? gameClassModel.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(this.uid);
        dest.writeString(this.classId);
        dest.writeString(this.raceId);
        dest.writeString(this.photoUrl);
        dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.gameRaceModel, flags);
        dest.writeParcelable(this.gameClassModel, flags);
    }

    protected GameCharacterModel(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.uid = in.readString();
        this.classId = in.readString();
        this.raceId = in.readString();
        this.photoUrl = in.readString();
        this.visible = in.readByte() != 0;
        this.user = in.readParcelable(User.class.getClassLoader());
        this.gameRaceModel = in.readParcelable(GameRaceModel.class.getClassLoader());
        this.gameClassModel = in.readParcelable(GameClassModel.class.getClassLoader());
    }

    public static final Creator<GameCharacterModel> CREATOR = new Creator<GameCharacterModel>() {
        @Override
        public GameCharacterModel createFromParcel(Parcel source) {
            return new GameCharacterModel(source);
        }

        @Override
        public GameCharacterModel[] newArray(int size) {
            return new GameCharacterModel[size];
        }
    };
}
      