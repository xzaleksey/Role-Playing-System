package com.valyakinaleksey.roleplayingsystem.modules.userprofile.view;

import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel;
import eu.davidea.flexibleadapter.items.IFlexible;

import java.util.List;

public interface UserProfileView extends LceView<UserProfileViewModel> {
    void updateList(List<IFlexible> iFlexibles);

    void showPasswordDialog();

}
