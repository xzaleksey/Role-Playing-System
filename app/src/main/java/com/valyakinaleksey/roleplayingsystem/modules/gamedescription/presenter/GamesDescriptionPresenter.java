package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter;


import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionView;

public interface GamesDescriptionPresenter extends Provider<UserGetInteractor>,Presenter<GamesDescriptionView> {
    void joinGame();
}