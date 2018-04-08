package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceResult

interface DicePresenter : Presenter<DiceView> {
    fun saveCurrentDices()

    fun onDicesChanged()

    fun onDiceCollectionClicked(diceCollection: DiceCollection)

    fun deleteDiceCollection(diceCollection: DiceCollection)

    fun throwDices()

    fun switchBackToProgress()

    fun rethrowDices(diceResults: Set<DiceResult>)
}