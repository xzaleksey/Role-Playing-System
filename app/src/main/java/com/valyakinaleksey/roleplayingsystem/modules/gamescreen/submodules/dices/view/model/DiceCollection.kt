package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.util.*

class DiceCollection {
    private val dices = TreeMap<Dice, Int>()
    var id: String? = null

    fun addDices(dice: Dice, count: Int = 1) {
        dices.put(dice, count)
    }

    fun removeDices(dice: Dice, count: Int = 1) {
        dices[dice]?.let {
            val newValue = it - count

            if (newValue >= 0) {
                dices[dice] = newValue
            }
        }
    }

    fun getDices(): Map<Dice, Int> {
        return dices
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiceCollection

        if (dices != other.dices) return false
        return true
    }

    fun getMaxValue(): Int {
        return dices.entries.sumBy { it.key.maxValue * it.value }
    }

    override fun hashCode(): Int = dices.hashCode()
}