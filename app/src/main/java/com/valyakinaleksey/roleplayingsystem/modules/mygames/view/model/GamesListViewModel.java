package com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GamesListViewModel extends BaseRequestUpdateViewModel
    implements RequestUpdateViewModel, Parcelable, Serializable {

  private String toolbarTitle;
  private transient List<IFlexible> items = Collections.emptyList();

  public GamesListViewModel() {
    setNeedUpdate(true);
  }

  public String getToolbarTitle() {
    return toolbarTitle;
  }

  public void setToolbarTitle(String toolbarTitle) {
    this.toolbarTitle = toolbarTitle;
  }

  public List<IFlexible> getItems() {
    return items;
  }

  public void setItems(List<IFlexible> items) {
    this.items = items;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(this.toolbarTitle);
  }

  protected GamesListViewModel(Parcel in) {
    super(in);
    this.toolbarTitle = in.readString();
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
