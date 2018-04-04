package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter

import android.os.Bundle
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor.DiceInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceSingleCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel
import timber.log.Timber

class DicePresenterImpl constructor(
        private val diceInteractor: DiceInteractor
) : BasePresenter<DiceView, DiceViewModel>(), DicePresenter {

    override fun initNewViewModel(arguments: Bundle): DiceViewModel {
        val diceViewModel = DiceViewModel()
        val gameModel = arguments.getParcelable<GameModel>(GameModel.KEY)
        diceViewModel.gameModel = gameModel
        val defaultDicesModel = diceInteractor.getDefaultDicesModel()
        diceViewModel.diceItems = defaultDicesModel

        for (iFlexible in defaultDicesModel) {
            if (iFlexible is DiceSingleCollectionViewModel) {
                diceViewModel.singleDiceCollections.add(iFlexible.diceCollection)
            }
        }
        return diceViewModel
    }

    override fun onDiceCollectionClicked(diceCollection: DiceCollection) {

    }

    override fun getData() {
        super.getData()
        view.showContent()
        updateInProgressState()
        observeDiceCollections()
    }

    override fun onDicesChanged() {
        updateInProgressState()
    }

    private fun updateInProgressState() {
        updateSaveDicesBtnState()
        updateThrowBtnState()
    }

    private fun updateSaveDicesBtnState() {
        if (checkSameCollectionExists()) {
            return
        }

        for (diceCollection in viewModel.singleDiceCollections) {
            if (diceCollection.getDiceCount() > 0) {
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
                        viewModel.savedDiceCollections = data
                        viewModel.diceCollectionsItems = diceInteractor.mapDiceCollections(viewModel)
                        view.updateDiceCollections()
                    }
                }))
    }

    private fun addDiceCollection(diceCollection: DiceCollection) {
        compositeSubscription.add(
                diceInteractor.addDiceCollection(viewModel.gameModel.id, diceCollection)
                        .compose(RxTransformers.applySchedulers())
                        .subscribe(object : DataObserver<DiceCollection>() {
                            override fun onData(data: DiceCollection) {
                                Timber.d("Collection added")
                            }
                        }))
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
