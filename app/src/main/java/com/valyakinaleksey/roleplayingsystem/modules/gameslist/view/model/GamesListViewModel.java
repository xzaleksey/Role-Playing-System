package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

import java.io.Serializable;

public class GamesListViewModel implements EmptyViewModel, Parcelable, Serializable {

  private transient DatabaseReference reference;
  private CreateGameDialogViewModel createGameDialogViewModel;
  private PasswordDialogViewModel passwordDialogViewModel;
  private String toolbarTitle;
  private int gamesCount;

  public GamesListViewModel() {
  }

  public DatabaseReference getReference() {
    return reference;
  }

  public void setReference(DatabaseReference reference) {
    this.reference = reference;
  }

  public int getGamesCount() {
    return gamesCount;
  }

  public void setGamesCount(int gamesCount) {
    this.gamesCount = gamesCount;
  }

  @Override public boolean isEmpty() {
    return reference == null;
  }

  public void setCreateGameDialogData(CreateGameDialogViewModel dialogData) {
    this.createGameDialogViewModel = dialogData;
  }

  public PasswordDialogViewModel getPasswordDialogViewModel() {
    return passwordDialogViewModel;
  }

  public void setPasswordDialogViewModel(PasswordDialogViewModel passwordDialogViewModel) {
    this.passwordDialogViewModel = passwordDialogViewModel;
  }

  public CreateGameDialogViewModel getCreateGameDialogData() {
    return createGameDialogViewModel;
  }

  public String getToolbarTitle() {
    return toolbarTitle;
  }

  public void setToolbarTitle(String toolbarTitle) {
    this.toolbarTitle = toolbarTitle;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.createGameDialogViewModel, flags);
    dest.writeParcelable(this.passwordDialogViewModel, flags);
    dest.writeString(this.toolbarTitle);
    dest.writeInt(gamesCount);
  }

  protected GamesListViewModel(Parcel in) {
    this.createGameDialogViewModel =
        in.readParcelable(CreateGameDialogViewModel.class.getClassLoader());
    this.passwordDialogViewModel =
        in.readParcelable(PasswordDialogViewModel.class.getClassLoader());
    this.toolbarTitle = in.readString();
    this.gamesCount = in.readInt();
  }

  public static final Creator<GamesListViewModel> CREATOR = new Creator<GamesListViewModel>() {
    @Override public GamesListViewModel createFromParcel(Parcel source) {
      return new GamesListViewModel(source);
    }

    @Override public GamesListViewModel[] newArray(int size) {
      return new GamesListViewModel[size];
    }
  };
}
