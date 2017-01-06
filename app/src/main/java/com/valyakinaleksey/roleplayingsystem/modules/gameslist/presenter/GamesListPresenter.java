package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;


import com.valyakinaleksey.roleplayingsystem.core.interfaces.Provider;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

public interface GamesListPresenter extends Presenter<GamesListView>, Provider<UserGetInteractor> {
    void createGame(GameModel gameModel);

    void loadComplete();

}