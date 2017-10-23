package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.qualifiers.GameId;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.di.app.GlobalComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameClassesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRacesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di.ParentFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import dagger.Component;

@Component(dependencies = ParentFragmentComponent.class, modules = ParentGameModule.class)
@PerFragmentScope public interface ParentGameComponent
    extends HasPresenter<ParentGamePresenter>, GlobalComponent {

  void inject(ParentGameFragment gamesListFragment);

  ParentPresenter parentPresenter();

  @GameId String getGameId();

  CharactersRepository getCharactersRepository();

  GameClassesRepository getGameClassesRepository();

  GameRacesRepository getGameRacesRepository();

  GameCharactersInteractor getGameCharactersInteractor();
}
