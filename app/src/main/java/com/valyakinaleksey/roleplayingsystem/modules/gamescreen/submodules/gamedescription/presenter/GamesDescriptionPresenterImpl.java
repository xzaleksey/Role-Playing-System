package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.presenter.RestorablePresenter;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

@PerFragment
public class GamesDescriptionPresenterImpl extends BasePresenter<GamesDescriptionView, GamesDescriptionModel> implements GamesDescriptionPresenter, RestorablePresenter<GamesDescriptionModel> {

    private UserGetInteractor userGetInteractor;

    public GamesDescriptionPresenterImpl(UserGetInteractor userGetInteractor) {
        this.userGetInteractor = userGetInteractor;
    }

    @Override
    protected GamesDescriptionModel initNewViewModel(Bundle arguments) {
        GamesDescriptionModel gamesDescriptionModel = new GamesDescriptionModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gamesDescriptionModel.setToolbarTitle(gameModel.getName());
        return gamesDescriptionModel;
    }

    @Override
    public void restoreViewModel(GamesDescriptionModel viewModel) {
        super.restoreViewModel(viewModel);
    }


    @Override
    public void getData() {
        view.setData(viewModel);
        view.showContent();
    }

    @Override
    public UserGetInteractor getValue() {
        return userGetInteractor;
    }

    @Override
    public void onFabPressed() {

    }
}
