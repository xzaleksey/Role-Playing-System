package com.valyakinaleksey.roleplayingsystem.modules.mygames.di;

import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.MyGamesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.CreateNewGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.communication.MyGamesListCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.MyGamesListPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.MyGamesListPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.view.model.state.MyGamesListViewState;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class MyGamesListModule {

  private final static String VIEW_STATE_FILE_NAME = MyGamesListModule.class.getSimpleName();

  @Provides MyGamesListViewState provideViewState(ViewStateStorage storage) {
    return new MyGamesListViewState(storage);
  }

  @Provides @PerFragmentScope MyGamesListPresenter provideCommunicationBus(
      @Named(PRESENTER) MyGamesListPresenter presenter, MyGamesListViewState viewState) {
    return new MyGamesListCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @PerFragmentScope MyGamesListPresenter providePresenter(
      CreateNewGameInteractor createNewGameInteractor, UserGetInteractor userGetInteractor,
      ValidatePasswordInteractor validatePasswordInteractor,
      CheckUserJoinedGameInteractor checkUserJoinedGameInteractor, ParentPresenter parentPresenter,
      MyGamesInteractor myGamesInteractor) {
    return new MyGamesListPresenterImpl(createNewGameInteractor, userGetInteractor,
        validatePasswordInteractor, checkUserJoinedGameInteractor, parentPresenter,
        myGamesInteractor);
  }

  @Provides ViewStateStorage provideViewStateStorage(PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
    return new FileViewStateStorage(fullPath);
  }
}
