package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasPasswordViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.BaseRequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.RequestUpdateViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.PasswordDialogViewModel;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

public class UserProfileViewModel extends BaseRequestUpdateViewModel
        implements RequestUpdateViewModel, Parcelable, Serializable, HasPasswordViewModel {
    public static final String CHANGE_USER_NAME = "CHANGE_USER_NAME";
    public static final String USER_ID = "user_id";

    private String displayName;
    private String email;
    private String userId;
    private String totalGamesCount;
    private String masterGamesCount;
    private String avatarUrl;
    private boolean isCurrentUser = false;
    private PasswordDialogViewModel passwordDialogViewModel;
    private transient List<IFlexible> items = Collections.emptyList();

    public UserProfileViewModel() {
        setNeedUpdate(true);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getTotalGamesCount() {
        return totalGamesCount;
    }

    public void setTotalGamesCount(String totalGamesCount) {
        this.totalGamesCount = totalGamesCount;
    }

    public String getMasterGamesCount() {
        return masterGamesCount;
    }

    public void setMasterGamesCount(String masterGamesCount) {
        this.masterGamesCount = masterGamesCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }



    @Override
    public PasswordDialogViewModel getPasswordDialogViewModel() {
        return passwordDialogViewModel;
    }

    @Override
    public void setPasswordDialogViewModel(PasswordDialogViewModel passwordDialogViewModel) {
        this.passwordDialogViewModel = passwordDialogViewModel;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.displayName);
        dest.writeString(this.email);
        dest.writeString(this.userId);
        dest.writeString(this.totalGamesCount);
        dest.writeString(this.masterGamesCount);
        dest.writeString(this.avatarUrl);
        dest.writeByte(this.isCurrentUser ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.passwordDialogViewModel, flags);
    }

    protected UserProfileViewModel(Parcel in) {
        super(in);
        this.displayName = in.readString();
        this.email = in.readString();
        this.userId = in.readString();
        this.totalGamesCount = in.readString();
        this.masterGamesCount = in.readString();
        this.avatarUrl = in.readString();
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
