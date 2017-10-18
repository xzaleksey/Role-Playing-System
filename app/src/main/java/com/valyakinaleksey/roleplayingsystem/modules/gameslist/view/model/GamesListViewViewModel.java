package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasCreateGameViewModel;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasPasswordViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GamesListViewViewModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable, HasPasswordViewModel,
    HasCreateGameViewModel {

  private CreateGameDialogViewModel createGameDialogViewModel;
  private PasswordDialogViewModel passwordDialogViewModel;
  private String toolbarTitle;
  private transient List<IFlexible<?>> items = Collections.emptyList();
  private int gamesCount;

  public GamesListViewViewModel() {
    setNeedUpdate(true);
  }

  public List<IFlexible<?>> getItems() {
    return items;
  }

  public void setItems(List<IFlexible<?>> items) {
    this.items = items;
  }

  public int getGamesCount() {
    return gamesCount;
  }

  public void setGamesCount(int gamesCount) {
    this.gamesCount = gamesCount;
  }

  public void setCreateGameDialogViewModel(CreateGameDialogViewModel dialogData) {
    this.createGameDialogViewModel = dialogData;
  }

  public PasswordDialogViewModel getPasswordDialogViewModel() {
    return passwordDialogViewModel;
  }

  public void setPasswordDialogViewModel(PasswordDialogViewModel passwordDialogViewModel) {
    this.passwordDialogViewModel = passwordDialogViewModel;
  }

  public CreateGameDialogViewModel getCreateGameDialogViewModel() {
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
    super.writeToParcel(dest, flags);
    dest.writeParcelable(this.createGameDialogViewModel, flags);
    dest.writeParcelable(this.passwordDialogViewModel, flags);
    dest.writeString(this.toolbarTitle);
    dest.writeInt(this.gamesCount);
  }

  protected GamesListViewViewModel(Parcel in) {
    super(in);
    this.createGameDialogViewModel =
        in.readParcelable(CreateGameDialogViewModel.class.getClassLoader());
    this.passwordDialogViewModel =
        in.readParcelable(PasswordDialogViewModel.class.getClassLoader());
    this.toolbarTitle = in.readString();
    this.gamesCount = in.readInt();
  }

  public static final Creator<GamesListViewViewModel> CREATOR =
      new Creator<GamesListViewViewModel>() {
        @Override public GamesListViewViewModel createFromParcel(Parcel source) {
          return new GamesListViewViewModel(source);
        }

        @Override public GamesListViewViewModel[] newArray(int size) {
          return new GamesListViewViewModel[size];
        }
      };
}
