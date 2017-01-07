package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticFieldsSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.util.ArrayList;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;

@PerFragment
public class GamesDescriptionPresenterImpl extends BasePresenter<GamesDescriptionView, GamesDescriptionModel> implements GamesDescriptionPresenter {

    private UserGetInteractor userGetInteractor;

    public GamesDescriptionPresenterImpl(UserGetInteractor userGetInteractor) {
        this.userGetInteractor = userGetInteractor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GamesDescriptionModel initNewViewModel(Bundle arguments) {
        GamesDescriptionModel gamesDescriptionModel = new GamesDescriptionModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gamesDescriptionModel.setToolbarTitle(gameModel.getName());
        gamesDescriptionModel.setGameModel(gameModel);
        ArrayList<InfoSection> infoSections = new ArrayList<>();
        ArrayList<StaticItem> data = new ArrayList<>();
        data.add(new StaticItem(TYPE_DESCRIPTION, gameModel.getDescription()));
        infoSections.add(new StaticFieldsSection(data));
        gamesDescriptionModel.setInfoSections(infoSections);
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

}
