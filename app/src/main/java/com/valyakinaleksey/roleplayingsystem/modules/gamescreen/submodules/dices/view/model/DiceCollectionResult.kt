package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.io.Serializable
import java.util.*

class DiceCollectionResult : Serializable {
    private val diceResults = TreeMap<Dice, MutableList<DiceResult>>()

    private fun addDiceResult(diceResult: DiceResult) {
        val dice = diceResult.dice
        var dicesResultsList = diceResults[dice]
        if (dicesResultsList == null) {
            dicesResultsList = mutableListOf()
            diceResults[dice] = dicesResultsList
        }

        dicesResultsList.add(diceResult)
    }

    fun getDiceResults(): Map<Dice, MutableList<DiceResult>> {
        return diceResults
    }

    fun resetResult() {
        diceResults.clear()
    }
}