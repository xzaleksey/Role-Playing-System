package com.valyakinaleksey.roleplayingsystem.modules.gameslist.view;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.CreateGameView;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.GamesListViewViewModel;

public interface GamesListView extends LceView<GamesListViewViewModel>, CreateGameView {
  void updateGamesCount();
}
