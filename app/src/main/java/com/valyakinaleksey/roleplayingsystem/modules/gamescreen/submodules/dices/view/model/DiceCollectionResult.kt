package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.io.Serializable
import java.util.*

class DiceCollectionResult(var id: String? = null) : Serializable {
    private val diceResults = TreeMap<Dice, MutableList<DiceResult>>()
    private var diceResultMax = 0

    constructor() : this(null)

    fun addDiceResult(diceResult: DiceResult) {
        val dice = diceResult.dice
        var dicesResultsList = diceResults[dice]
        diceResultMax += diceResult.dice.maxValue

        if (dicesResultsList == null) {
            dicesResultsList = mutableListOf()
            diceResults[dice] = dicesResultsList
        }

        dicesResultsList.add(diceResult)
    }

    fun getDiceResults(): Map<Dice, MutableList<DiceResult>> {
        return diceResults
    }

    fun getCurrentResult(): Int {
        var counter = 0
        for (diceResultList in diceResults.values) {
            for (diceResult in diceResultList) {
                counter += diceResult.value
            }
        }

        return counter
    }

    fun getMaxResult(): Int {
        return diceResultMax
    }

    fun resetResult() {
        diceResults.clear()
    }
}