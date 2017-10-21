package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;

public interface GamesCharactersPresenter extends Presenter<GamesCharactersView> {
  void createCharacter();

  void play(Context context, AbstractGameCharacterListItem abstractGameCharacterListItem);

  void changeNpcVisibility(GameCharacterModel gameCharacterModel, boolean isVisible);
}