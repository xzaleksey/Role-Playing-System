package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.presenter;

import android.os.Bundle;

import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver;
import com.valyakinaleksey.roleplayingsystem.core.rx.SkipObserver;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor.MasterLogInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.MasterLogView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.view.model.MasterLogModel;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

public class MasterLogPresenterImpl extends BasePresenter<MasterLogView, MasterLogModel>
        implements MasterLogPresenter {

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
        return masterLogModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        if (viewModel.isUpdatedRequired()) {
            super.getData();
            compositeSubscription.add(masterLogInteractor.observeMessages(viewModel.getGameModel().getId())
                    .compose(RxTransformers.applySchedulers())
                    .subscribe(new DataObserver<List<IFlexible<?>>>() {
                        @Override
                        public void onData(List<IFlexible<?>> data) {
                            viewModel.setItems(data);
                            view.showContent();
                        }
                    }));
        }
    }

    @Override
    public void sendMessage(String s) {
        MasterLogMessage message = new MasterLogMessage(s);
        masterLogInteractor.sendMessage(viewModel.getGameModel(), message)
                .compose(RxTransformers.applyIoSchedulers())
                .subscribe(new SkipObserver<>());
    }

    @Override
    public void restoreViewModel(MasterLogModel viewModel) {
        super.restoreViewModel(viewModel);
    }
}
