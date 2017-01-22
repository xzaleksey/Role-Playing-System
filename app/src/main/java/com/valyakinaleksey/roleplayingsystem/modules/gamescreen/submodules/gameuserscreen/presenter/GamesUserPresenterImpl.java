package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.presenter;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.MaterialDrawableProviderImpl;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
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
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.GamesUserView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gameuserscreen.view.model.GamesUserModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants;

import java.util.ArrayList;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_USERS_EXPANDABLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TITLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_LINE_WITH_AVATAR;

@PerFragmentScope
public class GamesUserPresenterImpl extends BasePresenter<GamesUserView, GamesUserModel> implements GamesUserPresenter {

    private UserGetInteractor userGetInteractor;
    private ObserveGameInteractor observeGameInteractor;
    private ObserveUsersInGameInteractor observeUsersInGameInteractor;

    public GamesUserPresenterImpl(UserGetInteractor userGetInteractor, ObserveGameInteractor observeGameInteractor, ObserveUsersInGameInteractor observeUsersInGameInteractor) {
        this.userGetInteractor = userGetInteractor;
        this.observeGameInteractor = observeGameInteractor;
        this.observeUsersInGameInteractor = observeUsersInGameInteractor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GamesUserModel initNewViewModel(Bundle arguments) {
        final GamesUserModel gamesUserModel = new GamesUserModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gamesUserModel.setToolbarTitle(gameModel.getName());
        gamesUserModel.setGameModel(gameModel);

        return gamesUserModel;
    }

    @Override
    public void restoreViewModel(GamesUserModel viewModel) {
        super.restoreViewModel(viewModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        GameModel gameModel = viewModel.getGameModel();
        compositeSubscription.add(userGetInteractor.getUserByUid(gameModel.getMasterId())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .doOnNext(user -> {
                    updateInfoSections(gameModel, user);
                })
                .subscribe(user -> {
                    view.setData(viewModel);
                    view.showContent();
                    compositeSubscription.add(observeGameInteractor.observeGameModelChanged(viewModel.getGameModel())
                            .subscribe(gameModel1 -> {
                                        ArrayList<StaticItem> data = ((StaticFieldsSection) viewModel.getInfoSections().get(0)).getData();
                                        data.get(0).setValue(gameModel.getDescription());
                                        viewModel.setToolbarTitle(gameModel.getName());
                                        view.updateView();
                                    },
                                    Crashlytics::logException));
                    compositeSubscription.add(observeUsersInGameInteractor.observeUserJoinedToGame(viewModel.getGameModel().getId())
                            .subscribe(userInGameModel -> {
                                        User userModel = userInGameModel.getUser();
                                        for (InfoSection infoSection : viewModel.getInfoSections()) {
                                            if (infoSection.getSectionType() == ELEMENT_TYPE_USERS_EXPANDABLE) {
                                                ArrayList<AvatarWithTwoLineTextModel> data = infoSection.getData();
                                                switch (userInGameModel.getEventType()) {
                                                    case ADDED:
                                                        addUser(data, userModel);
                                                        break;
                                                    case REMOVED:
                                                        for (AvatarWithTwoLineTextModel avatarWithTwoLineTextModel : data) {
                                                            if (avatarWithTwoLineTextModel.getModel().equals(userModel)) {
                                                                data.remove(avatarWithTwoLineTextModel);
                                                                break;
                                                            }
                                                        }
                                                        break;
                                                }
                                                break;
                                            }
                                        }
                                        view.updateView();
                                    },
                                    Crashlytics::logException));
                }, Crashlytics::logException));
    }

    @Override
    public UserGetInteractor getValue() {
        return userGetInteractor;
    }

    @SuppressWarnings("unchecked")
    private void updateInfoSections(GameModel gameModel, User user) {
        ArrayList<InfoSection> infoSections = new ArrayList<>();
        ArrayList<StaticItem> data = new ArrayList<>();
        data.add(new StaticItem(TYPE_DESCRIPTION, gameModel.getDescription()));
        data.add(new StaticItem(TYPE_TITLE, RpsApp.app().getString(R.string.master_of_the_game)));
        data.add(new StaticItem(TYPE_TWO_LINE_WITH_AVATAR, new AvatarWithTwoLineTextModel(gameModel.getMasterName(), "Провел много игр",
                new MaterialDrawableProviderImpl(gameModel.getMasterName(), gameModel.getMasterId()), user.getPhotoUrl(), gameModel.getMasterId())));
        infoSections.add(new StaticFieldsSection(data));
        ArrayList<TwoOrThreeLineTextModel> twoOrThreeLineTextModels = new ArrayList<>();
        twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel("name1", "description1"));
        twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel("name2", "description2"));
        infoSections.add(new DefaultExpandableSectionImpl(RpsApp.app().getString(R.string.characteristics), twoOrThreeLineTextModels));
        ArrayList<AvatarWithTwoLineTextModel> avatarWithTwoLineTextModels = new ArrayList<>();
        infoSections.add(new TwoLineTextWithAvatarExpandableSectionImpl(AdapterConstants.ELEMENT_TYPE_USERS_EXPANDABLE, RpsApp.app().getString(R.string.game_players), avatarWithTwoLineTextModels));
        viewModel.setInfoSections(infoSections);
    }

    private void addUser(ArrayList<AvatarWithTwoLineTextModel> avatarWithTwoLineTextModels, User userModel) {
        avatarWithTwoLineTextModels.add(new AvatarWithTwoLineTextModel(userModel.getName(), "Провел много игр", new MaterialDrawableProviderImpl(userModel.getName(), userModel.getUid()), userModel.getPhotoUrl(), userModel));
    }
}
