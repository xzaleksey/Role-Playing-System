package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter

import android.os.Bundle
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor.DiceInteractor
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel

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
            if (iFlexible is DiceCollectionViewModel) {
                diceViewModel.singleDiceCollections.add(iFlexible.diceCollection)
            }
        }
        return diceViewModel
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
        for (savedDiceCollection in viewModel.savedDiceCollections) {
            if (savedDiceCollection.isSame(viewModel.singleDiceCollections)) {
                view.setSaveDicesEnabled(false)
                return
            }
        }

        for (diceCollection in viewModel.singleDiceCollections) {
            if (diceCollection.getDiceCount() > 0) {
                view.setSaveDicesEnabled(true)
                return
            }
        }

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

    override fun saveCurrentDices() {
        //TODO add implemetation
    }

}
