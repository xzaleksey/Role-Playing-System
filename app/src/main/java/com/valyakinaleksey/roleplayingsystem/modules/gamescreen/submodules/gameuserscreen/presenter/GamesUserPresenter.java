package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.presenter;


import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserView;

public interface GamesUserPresenter extends Presenter<GamesUserView>, Provider<UserGetInteractor> {
}