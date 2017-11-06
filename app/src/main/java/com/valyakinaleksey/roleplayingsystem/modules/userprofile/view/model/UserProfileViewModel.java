package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasPasswordViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;
import eu.davidea.flexibleadapter.items.IFlexible;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class UserProfileViewModel extends BaseRequestUpdateViewModel
        implements RequestUpdateViewModel, Parcelable, Serializable, HasPasswordViewModel {

    public static final String USER_ID = "user_id";

    private String toolbarTitle;
    private String userId;
    private boolean isCurrentUser = false;
    private PasswordDialogViewModel passwordDialogViewModel;
    private transient List<IFlexible> items = Collections.emptyList();

    public UserProfileViewModel() {
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


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        isCurrentUser = currentUser;
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
        dest.writeString(this.userId);
        dest.writeByte(this.isCurrentUser ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.passwordDialogViewModel, flags);
    }

    protected UserProfileViewModel(Parcel in) {
        super(in);
        this.toolbarTitle = in.readString();
        this.userId = in.readString();
        this.isCurrentUser = in.readByte() != 0;
        this.passwordDialogViewModel = in.readParcelable(PasswordDialogViewModel.class.getClassLoader());
    }

    public static final Creator<UserProfileViewModel> CREATOR = new Creator<UserProfileViewModel>() {
        @Override
        public UserProfileViewModel createFromParcel(Parcel source) {
            return new UserProfileViewModel(source);
        }

        @Override
        public UserProfileViewModel[] newArray(int size) {
            return new UserProfileViewModel[size];
        }
    };
}
