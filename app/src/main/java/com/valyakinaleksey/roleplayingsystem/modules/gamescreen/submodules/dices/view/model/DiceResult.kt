package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.io.Serializable

class DiceResult(val dice: Dice, var value: Int) : Serializable {

    fun rethrow() {
        value = dice.getRndValue()
    }

    companion object {
        fun throwDice(dice: Dice): DiceResult {
            return DiceResult(dice, dice.getRndValue())
        }
    }
}