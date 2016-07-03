package com.valyakinaleksey.roleplayingsystem.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Data to be transferred to dialog
 */
public class CautionDialogData implements Serializable, Parcelable {

    private int currentTemp;

    public CautionDialogData(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentTemp);
    }

    private CautionDialogData(Parcel in) {
        this.currentTemp = in.readInt();
    }

    public static final Creator<CautionDialogData> CREATOR = new Creator<CautionDialogData>() {
        public CautionDialogData createFromParcel(Parcel source) {
            return new CautionDialogData(source);
        }

        public CautionDialogData[] newArray(int size) {
            return new CautionDialogData[size];
        }
    };
}
