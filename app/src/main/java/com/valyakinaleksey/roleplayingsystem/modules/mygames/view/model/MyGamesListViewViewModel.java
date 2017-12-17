package com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasCreateGameViewModel;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasPasswordViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

public class MyGamesListViewViewModel extends BaseRequestUpdateViewModel
        implements RequestUpdateViewModel, Parcelable, Serializable, HasPasswordViewModel,
        HasCreateGameViewModel {

    private String toolbarTitle;
    private CreateGameDialogViewModel createGameDialogViewModel;
    private PasswordDialogViewModel passwordDialogViewModel;
    private transient List<IFlexible> items = Collections.emptyList();
    private GamesFilterModel filterModel = new GamesFilterModel();

    public MyGamesListViewViewModel() {
        setNeedUpdate(true);
    }

    public GamesFilterModel getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(GamesFilterModel filterModel) {
        this.filterModel = filterModel;
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

    public CreateGameDialogViewModel getCreateGameDialogViewModel() {
        return createGameDialogViewModel;
    }

    public void setCreateGameDialogViewModel(CreateGameDialogViewModel createGameDialogViewModel) {
        this.createGameDialogViewModel = createGameDialogViewModel;
    }

    @Override
    public PasswordDialogViewModel getPasswordDialogViewModel() {
        return passwordDialogViewModel;
    }

    @Override
    public void setPasswordDialogViewModel(PasswordDialogViewModel passwordDialogViewModel) {
        this.passwordDialogViewModel = passwordDialogViewModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.toolbarTitle);
        dest.writeParcelable(this.createGameDialogViewModel, flags);
        dest.writeParcelable(this.passwordDialogViewModel, flags);
        dest.writeSerializable(this.filterModel);
    }

    protected MyGamesListViewViewModel(Parcel in) {
        super(in);
        this.toolbarTitle = in.readString();
        this.createGameDialogViewModel = in.readParcelable(CreateGameDialogViewModel.class.getClassLoader());
        this.passwordDialogViewModel = in.readParcelable(PasswordDialogViewModel.class.getClassLoader());
        this.filterModel = (GamesFilterModel) in.readSerializable();
    }

    public static final Creator<MyGamesListViewViewModel> CREATOR = new Creator<MyGamesListViewViewModel>() {
        @Override
        public MyGamesListViewViewModel createFromParcel(Parcel source) {
            return new MyGamesListViewViewModel(source);
        }

        @Override
        public MyGamesListViewViewModel[] newArray(int size) {
            return new MyGamesListViewViewModel[size];
        }
    };
}
