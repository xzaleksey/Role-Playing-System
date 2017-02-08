package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.communication;

import android.content.Context;
import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.state.GamesCharactersViewState;

import javax.inject.Inject;

@PerFragmentScope public class GameCharactersViewCommunicationBus extends
    SelfRestorableNavigationLceCommunicationBus<GamesCharactersViewModel, GamesCharactersView, GamesCharactersPresenter, GamesCharactersViewState>
    implements GamesCharactersPresenter, GamesCharactersView {

  @Override public void attachView(GamesCharactersView view) {
    super.attachView(view);
  }

  @Inject public GameCharactersViewCommunicationBus(GamesCharactersPresenter presenter,
      GamesCharactersViewState viewState) {
    super(presenter, viewState);
  }

  @Override public void updateView() {
    if (view != null) {
      view.updateView();
    }
  }

  @Override public void createCharacter() {
    presenter.createCharacter();
  }

  @Override
  public void play(Context context, AbstractGameCharacterListItem abstractGameCharacterListItem) {
    presenter.play(context, abstractGameCharacterListItem);
  }
}
