package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter;

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
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.adapter.MasterGameSection;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.*;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_USERS_EXPANDABLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TITLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_LINE_WITH_AVATAR;


public class MasterGameEditPresenterImpl extends BasePresenter<MasterGameEditView, MasterGameEditModel> implements MasterGameEditPresenter {

    private UserGetInteractor userGetInteractor;
    private ObserveGameInteractor observeGameInteractor;
    private ObserveUsersInGameInteractor observeUsersInGameInteractor;

    public MasterGameEditPresenterImpl(UserGetInteractor userGetInteractor, ObserveGameInteractor observeGameInteractor, ObserveUsersInGameInteractor observeUsersInGameInteractor) {
        this.userGetInteractor = userGetInteractor;
        this.observeGameInteractor = observeGameInteractor;
        this.observeUsersInGameInteractor = observeUsersInGameInteractor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MasterGameEditModel initNewViewModel(Bundle arguments) {
        final MasterGameEditModel masterGameEditModel = new MasterGameEditModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        masterGameEditModel.setGameModel(gameModel);
        return masterGameEditModel;
    }

    @Override
    public void restoreViewModel(MasterGameEditModel viewModel) {
        super.restoreViewModel(viewModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        compositeSubscription.add(Observable.just(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .map(gameModel -> {
                    ArrayList<InfoSection> infoSections = new ArrayList<>();
                    ArrayList<StaticItem> data = new ArrayList<>();
                    SingleValueEditModel singleValueEditModel = new SingleValueEditModel();
                    singleValueEditModel.setTitle(RpsApp.app().getString(R.string.name));
                    data.add(new StaticItem(MASTER_GAME_DESCRIPTION, singleValueEditModel));
                    MasterGameSection section = new MasterGameSection(data);
                    infoSections.add(section);
                    return infoSections;
                }).subscribe(infoSections -> {
                    viewModel.setInfoSections(infoSections);
                    view.setData(viewModel);
                    view.showContent();
                }, Crashlytics::logException));
    }
}
