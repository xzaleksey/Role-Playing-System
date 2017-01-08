package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.presenter;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.MaterialDrawableProviderImpl;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragment;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.DefaultExpandableSectionImpl;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticFieldsSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.AvatarWithTwoLineTextModel;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.util.ArrayList;

import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DIVIDER;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TITLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_LINE_WITH_AVATAR;

@PerFragment
public class GamesDescriptionPresenterImpl extends BasePresenter<GamesDescriptionView, GamesDescriptionModel> implements GamesDescriptionPresenter {

    private UserGetInteractor userGetInteractor;
    private JoinGameInteractor joinGameInteractor;

    public GamesDescriptionPresenterImpl(UserGetInteractor userGetInteractor, JoinGameInteractor joinGameInteractor) {
        this.userGetInteractor = userGetInteractor;
        this.joinGameInteractor = joinGameInteractor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GamesDescriptionModel initNewViewModel(Bundle arguments) {
        final GamesDescriptionModel gamesDescriptionModel = new GamesDescriptionModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gamesDescriptionModel.setToolbarTitle(gameModel.getName());
        gamesDescriptionModel.setGameModel(gameModel);

        return gamesDescriptionModel;
    }

    @Override
    public void restoreViewModel(GamesDescriptionModel viewModel) {
        super.restoreViewModel(viewModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        GameModel gameModel = viewModel.getGameModel();
        compositeSubscription.add(userGetInteractor.getUserByUid(gameModel.getMasterId())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(user -> {
                    ArrayList<InfoSection> infoSections = new ArrayList<>();
                    ArrayList<StaticItem> data = new ArrayList<>();
                    data.add(new StaticItem(TYPE_DESCRIPTION, gameModel.getDescription()));
                    data.add(new StaticItem(TYPE_TITLE, RpsApp.app().getString(R.string.master_of_the_game)));
                    data.add(new StaticItem(TYPE_TWO_LINE_WITH_AVATAR, new AvatarWithTwoLineTextModel(gameModel.getMasterName(), "Провел много игр",
                            new MaterialDrawableProviderImpl(gameModel.getMasterName(), gameModel.getMasterId()), user.getPhotoUrl())));
                    data.add(new StaticItem(TYPE_DIVIDER, null));
                    infoSections.add(new StaticFieldsSection(data));
                    ArrayList<TwoOrThreeLineTextModel> twoOrThreeLineTextModels = new ArrayList<>();
                    twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel("name1", "description1"));
                    twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel("name2", "description2"));
                    infoSections.add(new DefaultExpandableSectionImpl(RpsApp.app().getString(R.string.characteristics), twoOrThreeLineTextModels));
                    viewModel.setInfoSections(infoSections);
                    view.setData(viewModel);
                    view.showContent();
                }, Crashlytics::logException));
    }

    @Override
    public UserGetInteractor getValue() {
        return userGetInteractor;
    }

    @Override
    public void joinGame() {
        compositeSubscription.add(joinGameInteractor
                .joinGame(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .subscribe(aBoolean -> {

                }, throwable -> {
                    Timber.d(throwable);
                    Crashlytics.logException(throwable);
                }));
    }
}
