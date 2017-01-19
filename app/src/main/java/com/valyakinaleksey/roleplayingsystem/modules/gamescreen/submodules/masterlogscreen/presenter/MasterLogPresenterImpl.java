package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import org.joda.time.DateTime;


public class MasterLogPresenterImpl extends BasePresenter<MasterLogView, MasterLogModel> implements MasterLogPresenter {

    private MasterLogInteractor masterLogInteractor;

    public MasterLogPresenterImpl(MasterLogInteractor masterLogInteractor) {
        this.masterLogInteractor = masterLogInteractor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MasterLogModel initNewViewModel(Bundle arguments) {
        final MasterLogModel masterLogModel = new MasterLogModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        masterLogModel.setGameModel(gameModel);
        setDatabaseReference(masterLogModel);
        return masterLogModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        view.setData(viewModel);
        view.showContent();
        view.showLoading();
        compositeSubscription.add(masterLogInteractor.checkLogExists(viewModel.getGameModel())
                .compose(RxTransformers.applySchedulers())
                .subscribe(exists -> {
                    if (!exists) {
                        view.hideLoading();
                    }
                }, Crashlytics::logException));
    }

    @Override
    public void loadComplete() {
        view.hideLoading();
    }

    @Override
    public void sendMessage(String s) {
        MasterLogMessage message = new MasterLogMessage();
        message.setText(s);
        message.setDateCreate(DateTime.now().getMillis());
        masterLogInteractor.sendMessage(viewModel.getGameModel(), message)
                .compose(RxTransformers.applyIoSchedulers())
                .subscribe(masterLogMessage -> {

                }, Crashlytics::logException);
    }

    @Override
    public void restoreViewModel(MasterLogModel viewModel) {
        super.restoreViewModel(viewModel);
        setDatabaseReference(viewModel);
    }

    private void setDatabaseReference(MasterLogModel masterLogModel) {
        masterLogModel.setDatabaseReference(FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAME_LOG).child(masterLogModel.getGameModel().getId()));
    }
}
