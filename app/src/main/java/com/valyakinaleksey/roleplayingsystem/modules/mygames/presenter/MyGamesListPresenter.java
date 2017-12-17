package com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGamePresenter;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.MyGamesListView;

import eu.davidea.flexibleadapter.items.IFlexible;

public interface MyGamesListPresenter extends Presenter<MyGamesListView>, CreateGamePresenter {
    void loadComplete();

    void onFabPressed();

    void navigateToGameScreen(GameModel model);

    boolean onItemClicked(IFlexible<?> item);

    void onSearchQueryChanged(String queryText);
}