package com.valyakinaleksey.roleplayingsystem.modules.parentscreen.di;

import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.ParentScope;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.communication.ParentViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.ParentFragment;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.view.model.state.ParentGameViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;

import javax.inject.Named;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class ParentFragmentModule extends BaseFragmentModule {

  private final static String VIEW_STATE_FILE_NAME = "ParentFragmentModule";

  public ParentFragmentModule(String fragmentId) {
    super(fragmentId);
  }

  @Provides ParentGameViewState provideViewState(
      @Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
    return new ParentGameViewState(storage);
  }

  @AutoExpose(ParentFragment.class) @Provides @ParentScope ParentPresenter provideCommunicationBus(
      @Named(PRESENTER) ParentPresenter presenter, ParentGameViewState viewState) {
    return new ParentViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @ParentScope ParentPresenter providePresenter(
          GameRepository gameRepository, CheckUserJoinedGameInteractor checkUserInteractor) {
    return new ParentPresenterImpl(gameRepository, checkUserInteractor);
  }

  @Named(VIEW_STATE_FILE_NAME) @Provides ViewStateStorage provideViewStateStorage(
      PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
    return new FileViewStateStorage(fullPath);
  }
}
