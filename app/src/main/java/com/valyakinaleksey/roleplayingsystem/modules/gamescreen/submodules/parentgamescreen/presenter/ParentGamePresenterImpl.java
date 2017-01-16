package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;

public class ParentGamePresenterImpl extends BasePresenter<ParentView, ParentGameModel> implements ParentGamePresenter {


    private CheckUserJoinedGameInteractor checkUserJoinedGameInteractor;
    private ParentPresenter parentPresenter;

    public ParentGamePresenterImpl(CheckUserJoinedGameInteractor checkUserJoinedGameInteractor, ParentPresenter parentPresenter) {
        this.checkUserJoinedGameInteractor = checkUserJoinedGameInteractor;
        this.parentPresenter = parentPresenter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ParentGameModel initNewViewModel(Bundle arguments) {
        final ParentGameModel parentGameModel = new ParentGameModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        parentGameModel.setGameModel(gameModel);
        parentGameModel.setMaster(gameModel.getMasterId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        return parentGameModel;
    }

    @Override
    public void restoreViewModel(ParentGameModel viewModel) {
        super.restoreViewModel(viewModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        view.setData(viewModel);
        view.showContent();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GameModel gameModel = viewModel.getGameModel();
        compositeSubscription.add(
                checkUserJoinedGameInteractor
                        .checkUserInGame(currentUserId, gameModel)
                        .compose(RxTransformers.applySchedulers())
                        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                        .subscribe(aBoolean -> {
                            if (viewModel.isMaster()) {
                                viewModel.setNavigationTag(ParentGameModel.MASTER_SCREEN);
                            } else {
                                if (!aBoolean) {
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(GameModel.KEY, viewModel.getGameModel());
                                    parentPresenter.navigateToFragment(NavigationUtils.GAME_DESCRIPTION_FRAGMENT, bundle);
                                } else {
                                    viewModel.setNavigationTag(ParentGameModel.USER_SCREEN);
                                }
                            }
                            view.setData(viewModel);
                            viewModel.setFirstNavigation(false);
                            view.navigate();
                        }));

    }
}
