package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.state;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.StorageBackedNavigationLceViewStateImpl;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.UserProfileView;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel;

public class UserProfileViewState extends StorageBackedNavigationLceViewStateImpl<UserProfileViewModel, UserProfileView> {

    public UserProfileViewState(ViewStateStorage storage) {
        super(storage);
    }
}
