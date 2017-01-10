package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter;


import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterscreen.presenter.ChildGamePresenter;

public interface GamesDescriptionPresenter extends Provider<UserGetInteractor>, ChildGamePresenter<GamesDescriptionView> {
    void joinGame();
}