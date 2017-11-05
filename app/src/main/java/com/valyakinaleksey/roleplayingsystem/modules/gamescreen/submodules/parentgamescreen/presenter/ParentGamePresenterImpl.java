package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter;

import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.utils.SerializableTuple;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersGameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.GameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen;
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils;

import java.util.ArrayList;

public class ParentGamePresenterImpl extends BasePresenter<ParentView, ParentGameModel>
        implements ParentGamePresenter {

    private ParentPresenter parentPresenter;
    private GameInteractor gameInteractor;
    private CharactersGameRepository charactersRepository;

    public ParentGamePresenterImpl(ParentPresenter parentPresenter, GameInteractor gameInteractor, CharactersGameRepository charactersRepository) {
        this.parentPresenter = parentPresenter;
        this.gameInteractor = gameInteractor;
        this.charactersRepository = charactersRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParentGameModel initNewViewModel(Bundle arguments) {
        final ParentGameModel parentGameModel = new ParentGameModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        parentGameModel.setGameModel(gameModel);
        boolean isMaster = gameModel.getMasterId().equals(FireBaseUtils.getCurrentUserId());
        parentGameModel.setMaster(isMaster);
        if (!isMaster) {
            charactersRepository.updateLastPlayedGameCharacters(gameModel.getId())
                    .compose(RxTransformers.applyIoSchedulers())
                    .subscribe();
        }
        return parentGameModel;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(GameModel.KEY, viewModel.getGameModel());
    }

    @Override
    public void restoreViewModel(ParentGameModel viewModel) {
        super.restoreViewModel(viewModel);
        initSubscriptions(viewModel.getGameModel());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        view.setData(viewModel);
        view.preFillModel(viewModel);
        String currentUserId = FireBaseUtils.getCurrentUserId();
        GameModel gameModel = viewModel.getGameModel();
        if (viewModel.isUpdatedRequired()) {
            view.showLoading();
            compositeSubscription.add(gameInteractor.checkUserInGame(currentUserId, gameModel)
                    .compose(RxTransformers.applySchedulers())
                    .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                    .subscribe(aBoolean -> {
                        ArrayList<SerializableTuple<Integer, String>> fragmentsInfo =
                                viewModel.getFragmentsInfo();
                        fragmentsInfo.clear();
                        if (aBoolean) {
                            if (viewModel.isMaster()) {
                                viewModel.setNavigationTag(ParentGameModel.MASTER_SCREEN);
                                fragmentsInfo.add(new SerializableTuple<>(NavigationScreen.GAME_MASTER_EDIT_FRAGMENT,
                                        RpsApp.app().getString(R.string.info)));
                                fragmentsInfo.add(new SerializableTuple<>(NavigationScreen.GAME_MASTER_LOG_FRAGMENT,
                                        RpsApp.app().getString(R.string.log)));
                            }
                            fragmentsInfo.add(new SerializableTuple<>(NavigationScreen.GAME_CHARACTERS_FRAGMENT,
                                    RpsApp.app().getString(R.string.characters)));
                            fragmentsInfo.add(new SerializableTuple<>(NavigationScreen.GAME_MAPS_FRAGMENT,
                                    RpsApp.app().getString(R.string.maps)));
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(GameModel.KEY, viewModel.getGameModel());
                            bundle.putBoolean(NavigationUtils.POP_BACK_STACK, true);
                            bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true);
                            parentPresenter.navigateToFragment(NavigationScreen.GAME_DESCRIPTION_FRAGMENT, bundle);
                        }
                        if (view != null) {
                            view.setData(viewModel);
                            viewModel.setFirstNavigation(true);
                            view.showContent();
                            viewModel.setFirstNavigation(false);
                            view.setData(viewModel); // to save first nav state
                        }
                    }, this::handleThrowable));
        }

        initSubscriptions(gameModel);
    }

    private void initSubscriptions(GameModel gameModel) {
        compositeSubscription.add(
                gameInteractor.observeGameModelChanged(gameModel).subscribe(gameModel1 -> {
                    viewModel.setGameModel(gameModel1);
                    view.preFillModel(viewModel);
                }, Crashlytics::logException));
        compositeSubscription.add(gameInteractor.observeGameModelRemoved(gameModel)
                .subscribe(gameModel1 -> parentPresenter.navigateBack(), Crashlytics::logException));
    }

    @Override
    public void deleteGame() {
        compositeSubscription.add(gameInteractor.
                deleteGame(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aBoolean -> {
                }, Crashlytics::logException));
    }

    @Override
    public void finishGame() {
        compositeSubscription.add(gameInteractor.
                finishGame(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aVoid -> {
                    parentPresenter.navigateBack();
                }, Crashlytics::logException));
    }

    @Override
    public void leaveGame() {
        compositeSubscription.add(gameInteractor.
                leaveGame(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aVoid -> parentPresenter.navigateBack(), Crashlytics::logException));
    }

    @Override
    public void openGame() {
        compositeSubscription.add(gameInteractor.
                openGame(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(result -> {
                    viewModel.getGameModel().setFinished(false);
                    view.showContent();
                }, Crashlytics::logException));
    }
}
