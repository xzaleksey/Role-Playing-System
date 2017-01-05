package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;

public interface GamesListPresenter extends Presenter<GamesListView> {
    void createGame(GameModel gameModel);
}