package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;

import java.io.Serializable;

public class MasterLogMessage implements Serializable, Parcelable, HasId {
    public static final int MASTER_MESSAGE = 1;

    private String id;
    private String text;
    private int logType = MASTER_MESSAGE;
    private Object dateCreate;
    private Long tempDateCreate;
    private Attachment attachment;

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

    @Exclude
    public long getDateCreateLong() {
        if (dateCreate == null || dateCreate == ServerValue.TIMESTAMP) {
            return tempDateCreate;
        }
        return (long) dateCreate;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
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

    public MasterLogMessage(String text) {
        this.text = text;
        dateCreate = ServerValue.TIMESTAMP;
    }

    public MasterLogMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.text);
        dest.writeInt(this.logType);
        dest.writeSerializable((Serializable) this.dateCreate);
        dest.writeValue(this.tempDateCreate);
        dest.writeSerializable(attachment);
    }

    protected MasterLogMessage(Parcel in) {
        this.id = in.readString();
        this.text = in.readString();
        this.logType = in.readInt();
        this.dateCreate = in.readSerializable();
        this.tempDateCreate = (Long) in.readValue(Long.class.getClassLoader());
        this.attachment = (Attachment) in.readSerializable();
    }

    public static final Creator<MasterLogMessage> CREATOR = new Creator<MasterLogMessage>() {
        @Override
        public MasterLogMessage createFromParcel(Parcel source) {
            return new MasterLogMessage(source);
        }

        @Override
        public MasterLogMessage[] newArray(int size) {
            return new MasterLogMessage[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MasterLogMessage that = (MasterLogMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (logType != that.logType) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (dateCreate == null ? that.dateCreate != null : that.dateCreate == null) {
            return false;
        }
        if (tempDateCreate != null ? !tempDateCreate.equals(that.tempDateCreate) : that.tempDateCreate != null)
            return false;
        return attachment != null ? attachment.equals(that.attachment) : that.attachment == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + logType;
        result = 31 * result + (dateCreate != null ? dateCreate.hashCode() : 0);
        result = 31 * result + (tempDateCreate != null ? tempDateCreate.hashCode() : 0);
        result = 31 * result + (attachment != null ? attachment.hashCode() : 0);
        return result;
    }
}
      