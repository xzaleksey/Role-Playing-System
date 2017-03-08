package com.valyakinaleksey.roleplayingsystem.modules.mygames.view;

import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.GamesListViewModel;

public interface MyGamesListView extends LceView<GamesListViewModel> {

  void onGameCreated();

  void showCreateGameDialog();

  void showPasswordDialog();
}
