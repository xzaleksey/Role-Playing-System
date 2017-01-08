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
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.TwoLineTextWithAvatarExpandableSectionImpl;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.AvatarWithTwoLineTextModel;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TITLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_LINE_WITH_AVATAR;

@PerFragment
public class GamesDescriptionPresenterImpl extends BasePresenter<GamesDescriptionView, GamesDescriptionModel> implements GamesDescriptionPresenter {

    private UserGetInteractor userGetInteractor;
    private JoinGameInteractor joinGameInteractor;
    private ObserveGameInteractor observeGameInteractor;

    public GamesDescriptionPresenterImpl(UserGetInteractor userGetInteractor, JoinGameInteractor joinGameInteractor, ObserveGameInteractor observeGameInteractor) {
        this.userGetInteractor = userGetInteractor;
        this.joinGameInteractor = joinGameInteractor;
        this.observeGameInteractor = observeGameInteractor;
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
                .zipWith(userGetInteractor.getUsersByGameId(gameModel.getId()), (user, users) -> {
                    updateInfoSections(gameModel, user, users);
                    return user;
                })
                .subscribe(user -> {
                    view.setData(viewModel);
                    view.showContent();
                    compositeSubscription.add(observeGameInteractor.observeGameModel(viewModel.getGameModel())
                            .subscribe(gameModel1 -> {
                                        ArrayList<StaticItem> data = ((StaticFieldsSection) viewModel.getInfoSections().get(0)).getData();
                                        data.get(0).setValue(gameModel.getDescription());
                                        viewModel.setToolbarTitle(gameModel.getName());
                                        view.updateView();
                                    },
                                    Crashlytics::logException));
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

    @SuppressWarnings("unchecked")
    private void updateInfoSections(GameModel gameModel, User user, List<User> users) {
        ArrayList<InfoSection> infoSections = new ArrayList<>();
        ArrayList<StaticItem> data = new ArrayList<>();
        data.add(new StaticItem(TYPE_DESCRIPTION, gameModel.getDescription()));
        data.add(new StaticItem(TYPE_TITLE, RpsApp.app().getString(R.string.master_of_the_game)));
        data.add(new StaticItem(TYPE_TWO_LINE_WITH_AVATAR, new AvatarWithTwoLineTextModel(gameModel.getMasterName(), "Провел много игр",
                new MaterialDrawableProviderImpl(gameModel.getMasterName(), gameModel.getMasterId()), user.getPhotoUrl())));
        infoSections.add(new StaticFieldsSection(data));
        ArrayList<TwoOrThreeLineTextModel> twoOrThreeLineTextModels = new ArrayList<>();
        twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel("name1", "description1"));
        twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel("name2", "description2"));
        infoSections.add(new DefaultExpandableSectionImpl(RpsApp.app().getString(R.string.characteristics), twoOrThreeLineTextModels));
        ArrayList<AvatarWithTwoLineTextModel> avatarWithTwoLineTextModels = new ArrayList<>();
        for (User userModel : users) {
            avatarWithTwoLineTextModels.add(new AvatarWithTwoLineTextModel(userModel.getName(), "Провел много игр", new MaterialDrawableProviderImpl(userModel.getName(), userModel.getUid()), userModel.getPhotoUrl()));
        }
        infoSections.add(new TwoLineTextWithAvatarExpandableSectionImpl(RpsApp.app().getString(R.string.game_players), avatarWithTwoLineTextModels));
        viewModel.setInfoSections(infoSections);
    }
}
