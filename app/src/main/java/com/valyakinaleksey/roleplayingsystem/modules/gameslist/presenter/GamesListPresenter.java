package com.valyakinaleksey.roleplayingsystem.modules.gameslist.presenter;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGamePresenter;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.GamesListView;
import eu.davidea.flexibleadapter.items.IFlexible;

public interface GamesListPresenter extends Presenter<GamesListView>, CreateGamePresenter {

  void loadComplete();

  void onFabPressed();

  void navigateToGameScreen(Context context, GameModel model);

  boolean onItemClick(IFlexible<?> item);

  void onSearchQueryChanged(String queryText);
}