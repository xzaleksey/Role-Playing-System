package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.presenter;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.CheckUserJoinedGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserFragment;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.ParentView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.parentgamescreen.view.model.ParentGameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

@PerFragment
public class ParentGamePresenterImpl extends BasePresenter<ParentView, ParentGameModel> implements ParentGamePresenter {


    private CheckUserJoinedGameInteractor checkUserJoinedGameInteractor;

    public ParentGamePresenterImpl(CheckUserJoinedGameInteractor checkUserJoinedGameInteractor) {
        this.checkUserJoinedGameInteractor = checkUserJoinedGameInteractor;
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
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        GameModel gameModel = viewModel.getGameModel();
        checkUserJoinedGameInteractor
                .checkUserInGame(currentUserId, gameModel)
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aBoolean -> {
                    if (viewModel.isMaster()) {
                        viewModel.setNavigationTag("master");
                    } else {
                        if (!aBoolean) {
                            viewModel.setNavigationTag(GamesDescriptionFragment.TAG);
                        } else {
                            viewModel.setNavigationTag(GamesUserFragment.TAG);
                        }
                    }
                    view.setData(viewModel);
                    viewModel.setFirstNavigation(false);
                    view.navigate();
                });

    }

    @Override
    public void onGameJoined() {
        viewModel.setNavigationTag(GamesUserFragment.TAG);
        view.navigate();
    }
}
