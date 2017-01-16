package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.adapter.MasterGameSection;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import java.util.ArrayList;

import rx.Observable;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.*;


public class MasterGameEditPresenterImpl extends BasePresenter<MasterGameEditView, MasterGameEditModel> implements MasterGameEditPresenter {

    private UserGetInteractor userGetInteractor;
    private ObserveGameInteractor observeGameInteractor;
    private EditGameInteractor editGameInteractor;
    private ObserveUsersInGameInteractor observeUsersInGameInteractor;

    public MasterGameEditPresenterImpl(UserGetInteractor userGetInteractor, ObserveGameInteractor observeGameInteractor, EditGameInteractor editGameInteractor) {
        this.userGetInteractor = userGetInteractor;
        this.observeGameInteractor = observeGameInteractor;
        this.editGameInteractor = editGameInteractor;
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
        initOnClickListeners(viewModel.getInfoSections());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        compositeSubscription.add(Observable.just(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
                .map(gameModel -> {
                    ArrayList<InfoSection> infoSections = getInfoSections();
                    initOnClickListeners(infoSections);
                    return infoSections;
                }).subscribe(infoSections -> {
                    viewModel.setInfoSections(infoSections);
                    view.setData(viewModel);
                    view.showContent();
                }, Crashlytics::logException));
    }

    @SuppressWarnings("unchecked")
    private void initOnClickListeners(ArrayList<InfoSection> infoSections) {
        for (InfoSection infoSection : infoSections) {
            switch (infoSection.getSectionType()) {
                case ELEMENT_TYPE_MASTER_GAME_INFO:
                    ((StaticItem<SingleValueEditModel>) infoSection.getData().get(0)).getValue().setSaveOnclickListener(s -> {
                        GameModel gameModel = viewModel.getGameModel();
                        if (!gameModel.getName().equals(s)) {
                            gameModel.setName(s);
                            compositeSubscription.add(editGameInteractor.saveField(gameModel, GameModel.FIELD_NAME, gameModel.getName())
                                    .subscribe(s1 -> {
                                        Timber.d("Success save name " + s1);
                                    }, Crashlytics::logException));
                        }
                    });

                    ((StaticItem<SingleValueEditModel>) infoSection.getData().get(1)).getValue().setSaveOnclickListener(s -> {
                        GameModel gameModel = viewModel.getGameModel();
                        if (!gameModel.getDescription().equals(s)) {
                            gameModel.setDescription(s);
                            compositeSubscription.add(editGameInteractor.saveField(gameModel, GameModel.FIELD_DESCRIPTION, gameModel.getDescription())
                                    .subscribe(s1 -> {
                                        Timber.d("Success save name " + s1);
                                    }, Crashlytics::logException));
                        }
                    });
                    break;
            }
        }
    }


    @NonNull
    @SuppressWarnings("unchecked")
    private ArrayList<InfoSection> getInfoSections() {
        ArrayList<InfoSection> infoSections = new ArrayList<>();
        ArrayList<StaticItem> data = new ArrayList<>();
        data.add(new StaticItem(MASTER_GAME_NAME, getTitleModel()));
        data.add(new StaticItem(MASTER_GAME_DESCRIPTION, getDescriptionModel()));
        MasterGameSection section = new MasterGameSection(data);
        infoSections.add(section);
        return infoSections;
    }

    @NonNull
    private SingleValueEditModel getTitleModel() {
        SingleValueEditModel singleValueEditModel = new SingleValueEditModel();
        singleValueEditModel.setTitle(RpsApp.app().getString(R.string.name));
        singleValueEditModel.setValue(viewModel.getGameModel().getName());
        singleValueEditModel.setValueHint(RpsApp.app().getString(R.string.super_game));
        return singleValueEditModel;
    }

    @NonNull
    private SingleValueEditModel getDescriptionModel() {
        SingleValueEditModel singleValueEditModel = new SingleValueEditModel();
        singleValueEditModel.setTitle(RpsApp.app().getString(R.string.description));
        singleValueEditModel.setValue(viewModel.getGameModel().getDescription());
        singleValueEditModel.setValueHint(RpsApp.app().getString(R.string.here_could_be_description));
        return singleValueEditModel;
    }
}
