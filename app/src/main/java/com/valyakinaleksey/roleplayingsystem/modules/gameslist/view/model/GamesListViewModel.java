package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;
import java.util.List;

public class GamesListViewModel implements EmptyViewModel, Parcelable, Serializable {

    private transient DatabaseReference reference;
    private CreateGameDialogViewModel dialogData;

    public GamesListViewModel() {
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }


    @Override
    public boolean isEmpty() {
        return reference == null;
    }

    public void setDialogData(CreateGameDialogViewModel dialogData) {
        this.dialogData = dialogData;
    }

    public CreateGameDialogViewModel getDialogData() {
        return dialogData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.dialogData, flags);
    }

    protected GamesListViewModel(Parcel in) {
        this.dialogData = in.readParcelable(CreateGameDialogViewModel.class.getClassLoader());
    }

    public static final Creator<GamesListViewModel> CREATOR = new Creator<GamesListViewModel>() {
        @Override
        public GamesListViewModel createFromParcel(Parcel source) {
            return new GamesListViewModel(source);
        }

        @Override
        public GamesListViewModel[] newArray(int size) {
            return new GamesListViewModel[size];
        }
    };
}
