package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.di;

import autodagger.AutoExpose;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.GameScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.communication.MapsViewCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.interactor.MapsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.presenter.MapsPresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.MapsFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.view.model.state.MapsViewState;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module public class MapsModule {

  private final static String VIEW_STATE_FILE_NAME = MapsModule.class.getSimpleName();

  @Provides MapsViewState provideViewState(ViewStateStorage storage) {
    return new MapsViewState(storage);
  }

  @Provides @GameScope @AutoExpose(MapsFragment.class) MapsPresenter provideCommunicationBus(
      @Named(PRESENTER) MapsPresenter presenter, MapsViewState viewState) {
    return new MapsViewCommunicationBus(presenter, viewState);
  }

  @Provides @Named(PRESENTER) @GameScope MapsPresenter providePresenter(
      MapsInteractor mapsInteractor, ParentPresenter parentPresenter) {
    return new MapsPresenterImpl(mapsInteractor, parentPresenter);
  }

  @Provides ViewStateStorage provideViewStateStorage(PathManager manager) {
    String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME;
    return new FileViewStateStorage(fullPath);
  }
}
