package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.HasPresenter;
import com.valyakinaleksey.roleplayingsystem.core.qualifiers.GameId;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameClassesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRacesRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di.CopyFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di.CopyModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di.GamesCharactersFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.di.GamesCharactersModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di.MapsFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di.MapsModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di.MasterGameEditFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.di.MasterGameEditModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogFragmentComponent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.di.MasterLogModule;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter.ParentGamePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentGameFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import dagger.Subcomponent;

@Subcomponent(modules = ParentGameModule.class) @PerFragmentScope
public interface ParentGameComponent extends HasPresenter<ParentGamePresenter> {

  void inject(ParentGameFragment gamesListFragment);

  ParentPresenter parentPresenter();

  @GameId String getGameId();

  CharactersRepository getCharactersRepository();

  GameClassesRepository getGameClassesRepository();

  GameRacesRepository getGameRacesRepository();

  GameCharactersInteractor getGameCharactersInteractor();

  MasterLogFragmentComponent getMasterLogFragmentComponent(MasterLogModule masterLogModule);

  MapsFragmentComponent getMapsFragmentComponent(MapsModule mapsModule);

  GamesCharactersFragmentComponent getGamesCharactersFragmentComponent(
      GamesCharactersModule gamesCharactersModule);

  MasterGameEditFragmentComponent getMasterGameEditFragmentComponent(
      MasterGameEditModule masterGameEditModule);

  CopyFragmentComponent getCopyFragmentComponent(CopyModule copyModule);
}
