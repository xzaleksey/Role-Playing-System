package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection

interface DicePresenter : Presenter<DiceView> {
    fun saveCurrentDices()

    fun onDicesChanged()

    fun onDiceCollectionClicked(diceCollection: DiceCollection)
}