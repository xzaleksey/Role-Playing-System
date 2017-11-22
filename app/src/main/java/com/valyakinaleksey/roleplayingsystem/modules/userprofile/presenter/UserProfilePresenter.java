package com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.PasswordPresenter;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.UserProfileView;
import eu.davidea.flexibleadapter.items.IFlexible;

public interface UserProfilePresenter extends Presenter<UserProfileView>, PasswordPresenter {

    void navigateToGameScreen(GameModel model);

    boolean onItemClicked(IFlexible<?> item);

    void editProfile();

    void onEditSuccess();
}