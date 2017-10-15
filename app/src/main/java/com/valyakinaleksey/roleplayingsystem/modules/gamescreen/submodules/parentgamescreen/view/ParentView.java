package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view;


import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;

public interface ParentView extends LceView<ParentGameModel> {

  void invalidateOptionsMenu();

  void navigate();
}
