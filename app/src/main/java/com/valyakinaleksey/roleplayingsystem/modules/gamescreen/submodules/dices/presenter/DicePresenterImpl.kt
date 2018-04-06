package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter

import android.os.Bundle
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.rx.CompletableObserver
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor.DiceInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel
import rx.Subscription

class DicePresenterImpl constructor(
        private val diceInteractor: DiceInteractor
) : BasePresenter<DiceView, DiceViewModel>(), DicePresenter {

    override fun initNewViewModel(arguments: Bundle): DiceViewModel {
        val diceViewModel = DiceViewModel()
        val gameModel = arguments.getParcelable<GameModel>(GameModel.KEY)
        diceViewModel.gameModel = gameModel
        diceViewModel.singleDiceCollections = diceInteractor.getDefaultSingleDicesCollections()
        val defaultDicesModel = diceInteractor.mapDicesCollectionToDicesModel(diceViewModel.singleDiceCollections)
        diceViewModel.diceItems = defaultDicesModel

        return diceViewModel
    }

    override fun restoreViewModel(viewModel: DiceViewModel) {
        super.restoreViewModel(viewModel)
        viewModel.diceItems = diceInteractor.mapDicesCollectionToDicesModel(viewModel.singleDiceCollections)
        viewModel.diceCollectionsItems = diceInteractor.mapDiceCollections(viewModel)
    }

    override fun onDiceCollectionClicked(diceCollection: DiceCollection) {
        if (viewModel.selectedDiceCollection == diceCollection) {
            viewModel.selectedDiceCollection = null
            viewModel.singleDiceCollections = diceInteractor.getDefaultSingleDicesCollections()
            viewModel.diceItems = diceInteractor.mapDicesCollectionToDicesModel(viewModel.singleDiceCollections)
            view.updateDices(false)
        } else {
            viewModel.selectedDiceCollection = diceCollection
            viewModel.singleDiceCollections = diceCollection.toSingleDiceCollections()
            viewModel.diceItems = diceInteractor.mapDicesCollectionToDicesModel(viewModel.singleDiceCollections)
            view.updateDices(false)
        }

        updateDiceCollections()
        updateInProgressState()
    }

    override fun deleteDiceCollection(diceCollection: DiceCollection) {
        removeDiceCollection(diceCollection)
    }

    override fun getData() {
        super.getData()
        view.showContent()
        updateInProgressState()
        observeDiceCollections()
    }

    override fun onDicesChanged() {
        updateInProgressState()
        checkUpdateDiceCollections()
        view.setData(viewModel)
    }

    private fun checkUpdateDiceCollections() {
        for (savedDiceCollection in viewModel.savedDiceCollections) {
            if (savedDiceCollection.isSame(viewModel.singleDiceCollections)) {
                viewModel.selectedDiceCollection = savedDiceCollection
                updateDiceCollections()
                return
            }
        }
        viewModel.selectedDiceCollection = null
        updateDiceCollections()
    }

    private fun updateDiceCollections() {
        viewModel.diceCollectionsItems = diceInteractor.mapDiceCollections(viewModel)
        view.updateDiceCollections(false)
    }

    private fun updateInProgressState() {
        updateSaveDicesBtnState()
        updateThrowBtnState()
    }

    private fun updateSaveDicesBtnState() {
        if (checkSameCollectionExists()) {
            return
        }
        var counter = 0
        for (diceCollection in viewModel.singleDiceCollections) {
            counter += diceCollection.getDiceCount()
            if (counter > 1) {
                view.setSaveDicesEnabled(true)
                return
            }
        }

        disableSaveBtn()
    }

    private fun checkSameCollectionExists(): Boolean {
        for (savedDiceCollection in viewModel.savedDiceCollections) {
            if (savedDiceCollection.isSame(viewModel.singleDiceCollections)) {
                disableSaveBtn()
                return true
            }
        }

        return false
    }

    private fun disableSaveBtn() {
        view.setSaveDicesEnabled(false)
    }

    private fun updateThrowBtnState() {
        for (diceCollection in viewModel.singleDiceCollections) {
            if (diceCollection.getDiceCount() > 0) {
                view.setThrowBtnEnabled(true)
                return
            }
        }

        view.setThrowBtnEnabled(false)
    }

    private fun observeDiceCollections() {
        compositeSubscription.add(diceInteractor.observeDiceCollections(viewModel.gameModel.id)
                .compose(RxTransformers.applySchedulers())
                .subscribe(object : DataObserver<List<DiceCollection>>() {
                    override fun onData(data: List<DiceCollection>) {
                        val previousCollection = viewModel.savedDiceCollections
                        viewModel.savedDiceCollections = data
                        checkUpdateDiceCollections()

                        if (previousCollection.size <= data.size) {
                            view.scrollDiceCollectionsToStart()
                        }

                        updateSaveDicesBtnState()
                    }
                }))
    }

    private fun addDiceCollection(diceCollection: DiceCollection) {
        compositeSubscription.add(
                diceInteractor.addDiceCollection(viewModel.gameModel.id, diceCollection)
                        .compose(RxTransformers.applySchedulers())
                        .subscribe(object : DataObserver<DiceCollection>() {
                            override fun onData(data: DiceCollection) {

                            }
                        }))
    }

    private fun removeDiceCollection(diceCollection: DiceCollection) {
        diceInteractor.removeDiceCollection(viewModel.gameModel.id, diceCollection)
                .compose(RxTransformers.applyCompletableSchedulers())
                .subscribe(object : CompletableObserver() {
                    override fun onCompleted() {
                    }

                    override fun onSubscribe(s: Subscription) {
                        compositeSubscription.add(s)
                    }
                })
    }

    override fun saveCurrentDices() {
        if (checkSameCollectionExists()) {
            return
        }

        val diceCollection = DiceCollection.createDiceCollectionFromSingleDiceCollections(viewModel.singleDiceCollections)
        viewModel.savedDiceCollections.add(diceCollection)
        disableSaveBtn()
        addDiceCollection(diceCollection)
    }

}
