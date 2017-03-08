package com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.Query;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseShouldRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.ShouldRequestUpdateViewModel;
import java.io.Serializable;

public class GamesListViewModel extends BaseShouldRequestUpdateViewModel
    implements ShouldRequestUpdateViewModel, Parcelable, Serializable {

  private transient Query query;
  private String toolbarTitle;
  private int gamesCount;

  public GamesListViewModel() {
    setNeedUpdate(true);
  }

  public Query getQuery() {
    return query;
  }

  public void setQuery(Query query) {
    this.query = query;
  }

  public int getGamesCount() {
    return gamesCount;
  }

  public void setGamesCount(int gamesCount) {
    this.gamesCount = gamesCount;
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

    dest.writeString(this.toolbarTitle);
    dest.writeInt(this.gamesCount);
  }

  protected GamesListViewModel(Parcel in) {
    super(in);
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
