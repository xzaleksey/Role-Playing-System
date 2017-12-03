package com.valyakinaleksey.roleplayingsystem.modules.userprofile.communication;

import android.content.Context;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter.UserProfilePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.UserProfileView;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.UserProfileViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.state.UserProfileViewState;
import eu.davidea.flexibleadapter.items.IFlexible;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;

@PerFragmentScope
public class UserProfileCommunicationBus extends
        SelfRestorableNavigationLceCommunicationBus<UserProfileViewModel, UserProfileView, UserProfilePresenter, UserProfileViewState>
        implements UserProfilePresenter, UserProfileView {

    @Override
    public void attachView(UserProfileView view) {
        super.attachView(view);
    }

    @Inject
    public UserProfileCommunicationBus(UserProfilePresenter presenter,
                                       UserProfileViewState viewState) {
        super(presenter, viewState);
    }

    @Override
    public void navigateToGameScreen(GameModel model) {
        presenter.navigateToGameScreen(model);
    }

    @Override
    public boolean onItemClicked(IFlexible<?> item) {
        return presenter.onItemClicked(item);
    }

    @Override
    public void checkPassword(GameModel model) {
        presenter.checkPassword(model);
    }

    @Override
    public void validatePassword(Context context, String s, GameModel gameModel) {
        presenter.validatePassword(context, s, gameModel);
    }

    @Override
    public void showPasswordDialog() {
        getNavigationResolver().resolveNavigation(UserProfileView::showPasswordDialog);
    }

    @Override
    public void pickImage() {
        getNavigationResolver().resolveNavigation(UserProfileView::pickImage);
    }

    @Override
    public void updateList(List<IFlexible> iFlexibles) {
        getNavigationResolver().resolveNavigation((view) -> view.updateList(iFlexibles));
    }

    @Override
    public void editProfile() {
        presenter.editProfile();
    }

    @Override
    public void onEditName(String name) {
        presenter.onEditName(name);
    }

    @Override
    public void onSelectAvatar() {
        presenter.onSelectAvatar();
    }

    @Override
    public void avatarImageChosen(@NotNull ChosenImage chosenImage) {
        presenter.avatarImageChosen(chosenImage);
    }
}
