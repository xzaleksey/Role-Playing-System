package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;


import android.content.Context;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGamePresenter;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

public interface GamesListPresenter extends Presenter<GamesListView>, Provider<UserGetInteractor>,
    CreateGamePresenter {

    void loadComplete();

    void onFabPressed();

    void navigateToGameScreen(Context context, GameModel model);

}