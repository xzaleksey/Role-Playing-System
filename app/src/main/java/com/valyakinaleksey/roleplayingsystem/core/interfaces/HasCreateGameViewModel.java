package com.valyakinaleksey.roleplayingsystem.core.interfaces;

import com.valyakinaleksey.roleplayingsystem.modules.gameslist.view.model.CreateGameDialogViewModel;

public interface HasCreateGameViewModel {
  CreateGameDialogViewModel getCreateGameDialogViewModel();

  void setCreateGameDialogViewModel(CreateGameDialogViewModel createGameDialogViewModel);
}
