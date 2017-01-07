package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter;


import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;

public interface GamesDescriptionPresenter extends Presenter<GamesDescriptionView>, Provider<UserGetInteractor> {

    void onFabPressed();


}