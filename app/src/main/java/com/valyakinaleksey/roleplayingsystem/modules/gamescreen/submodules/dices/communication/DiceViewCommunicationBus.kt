package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.communication

import com.valyakinaleksey.roleplayingsystem.core.proxy.SelfRestorableNavigationLceCommunicationBus
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.state.DiceViewState

class DiceViewCommunicationBus(presenter: DicePresenter, viewState: DiceViewState)
    : SelfRestorableNavigationLceCommunicationBus<DiceViewModel, DiceView, DicePresenter, DiceViewState>(presenter, viewState), DicePresenter, DiceView {

    override fun updateDiceCollections(animate: Boolean) {
        view?.updateDiceCollections(animate)
    }

    override fun onDiceCollectionClicked(diceCollection: DiceCollection) {
        presenter.onDiceCollectionClicked(diceCollection)
    }

    override fun onDicesChanged() {
        presenter.onDicesChanged()
    }

    override fun setSaveDicesEnabled(b: Boolean) {
        view?.setSaveDicesEnabled(b)
    }

    override fun setThrowBtnEnabled(b: Boolean) {
        view?.setThrowBtnEnabled(b)
    }

    override fun saveCurrentDices() {
        presenter.saveCurrentDices()
    }
}
