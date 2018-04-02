package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.io.Serializable

class SingleDiceCollection(val dice: Dice, private var count: Int = 0) : Serializable {
    var id: String? = null

    fun addDices(count: Int = 1) {
        this.count += count
    }

    fun removeDices(count: Int = 1) {
        this.count = Math.max(0, this.count - count)
    }

    fun getDiceCount(): Int {
        return count
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleDiceCollection

        if (dice != other.dice) return false
        if (count != other.count) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dice.hashCode()
        result = 31 * result + count
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


    companion object {
        @JvmStatic
        fun createSingleDiceCollectionFromDice(dice: Dice): SingleDiceCollection {
            val diceCollection = SingleDiceCollection(dice, 0)
            return diceCollection
        }
    }

}