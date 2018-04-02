package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.DiceView

interface DicePresenter : Presenter<DiceView> {
    fun saveCurrentDices()

    fun onDicesChanged()
}