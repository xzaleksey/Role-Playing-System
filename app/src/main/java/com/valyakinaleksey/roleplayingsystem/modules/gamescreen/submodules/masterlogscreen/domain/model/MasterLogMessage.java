package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import java.io.Serializable;
import java.util.HashMap;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.TIMESTAMP;

public class MasterLogMessage implements Serializable, Parcelable {
  public static final int SIMPLE_LINK = 0;
  public static final int IMAGE_LINK = 1;
  public static final int MASTER_TYPE = 1;

  private String id;
  private String text;
  private HashMap<String, Object> timestampCreated;
  private String link;
  private int linkType;
  private int logType = MASTER_TYPE;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public int getLogType() {
    return logType;
  }

  public void setLogType(int logType) {
    this.logType = logType;
  }

  public void setText(String text) {
    this.text = text;
  }

  public HashMap<String, Object> getTimestampCreated() {
    return timestampCreated;
  }

  @Exclude public long getDateCreate() {
    return (long) timestampCreated.get(TIMESTAMP);
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public int getLinkType() {
    return linkType;
  }

  public void setLinkType(int linkType) {
    this.linkType = linkType;
  }

  public MasterLogMessage(String text) {
    this.text = text;
    timestampCreated = new HashMap<>();
    timestampCreated.put(TIMESTAMP, ServerValue.TIMESTAMP);
  }

  public MasterLogMessage() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.text);
    dest.writeSerializable(this.timestampCreated);
    dest.writeString(this.link);
    dest.writeInt(this.linkType);
    dest.writeInt(this.logType);
  }

  protected MasterLogMessage(Parcel in) {
    this.id = in.readString();
    this.text = in.readString();
    this.timestampCreated = (HashMap<String, Object>) in.readSerializable();
    this.link = in.readString();
    this.linkType = in.readInt();
    this.logType = in.readInt();
  }

  public static final Creator<MasterLogMessage> CREATOR = new Creator<MasterLogMessage>() {
    @Override public MasterLogMessage createFromParcel(Parcel source) {
      return new MasterLogMessage(source);
    }

    @Override public MasterLogMessage[] newArray(int size) {
      return new MasterLogMessage[size];
    }
  };
}
      