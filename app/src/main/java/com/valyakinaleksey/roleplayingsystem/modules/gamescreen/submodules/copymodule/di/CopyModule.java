package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.di;

import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.communication.CopyViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.presenter.CopyPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.presenter.CopyPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.copymodule.view.model.state.CopyViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class CopyModule extends BaseFragmentModule {

  private final static String VIEW_STATE_FILE_NAME = "CopyModule";

  public CopyModule(String fragmentId) {
    super(fragmentId);
  }

  @Provides CopyViewState provideViewState(@Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
    return new CopyViewState(storage);
  }

  @Provides @GameScope CopyPresenter provideCommunicationBus(
      @Named(PRESENTER) CopyPresenter presenter, CopyViewState viewState) {
    return new CopyViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @GameScope CopyPresenter providePresenter() {
    return new CopyPresenterImpl();
  }

  @Named(VIEW_STATE_FILE_NAME) @Provides ViewStateStorage provideViewStateStorage(
      PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
    return new FileViewStateStorage(fullPath);
  }
}
