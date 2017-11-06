package com.valyakinaleksey.roleplayingsystem.modules.userprofile.di;

import com.valyakinaleksey.roleplayingsystem.core.di.BaseFragmentModule;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.FileViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage.ViewStateStorage;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor.ValidatePasswordInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.UserProfilePresenterImpl;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.communication.UserProfileCommunicationBus;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.domain.UserProfileInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter.UserProfilePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.view.model.state.UserProfileViewState;
import com.valyakinaleksey.roleplayingsystem.utils.PathManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

import static com.valyakinaleksey.roleplayingsystem.utils.DiConstants.PRESENTER;

@Module
public class UserProfileModule extends BaseFragmentModule {

    private final static String VIEW_STATE_FILE_NAME = "UserProfileModule";

    public UserProfileModule(String fragmentId) {
        super(fragmentId);
    }

    @Provides
    UserProfileViewState provideViewState(
            @Named(VIEW_STATE_FILE_NAME) ViewStateStorage storage) {
        return new UserProfileViewState(storage);
    }

    @Provides
    @PerFragmentScope
    UserProfilePresenter provideCommunicationBus(
            @Named(PRESENTER) UserProfilePresenter presenter, UserProfileViewState viewState) {
        return new UserProfileCommunicationBus(presenter, viewState);
    }

    @Provides
    @Named(PRESENTER)
    @PerFragmentScope
    UserProfilePresenter providePresenter(
            ValidatePasswordInteractor validatePasswordInteractor,
            CheckUserJoinedGameInteractor checkUserJoinedGameInteractor, ParentPresenter parentPresenter,
            GameRepository gamesRepository, UserProfileInteractor userProfileInteractor) {
        return new UserProfilePresenterImpl(checkUserJoinedGameInteractor, validatePasswordInteractor,
                parentPresenter,
                userProfileInteractor,
                gamesRepository);
    }

    @Named(VIEW_STATE_FILE_NAME)
    @Provides
    ViewStateStorage provideViewStateStorage(
            PathManager manager) {
        String fullPath = manager.getCachePath() + VIEW_STATE_FILE_NAME + getFragmentId();
        return new FileViewStateStorage(fullPath);
    }
}
