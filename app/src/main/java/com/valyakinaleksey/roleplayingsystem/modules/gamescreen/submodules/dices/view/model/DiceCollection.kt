package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.io.Serializable
import java.util.*

class DiceCollection : Serializable {
    private val dices: MutableMap<Dice, Int> = TreeMap()
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

    fun totalDices(): Int {
        var count = 0
        for (entry in dices) {
            count += entry.value
        }
        return count
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

    fun isSame(singleDiceCollections: List<SingleDiceCollection>): Boolean {
        return singleDiceCollections.none { it.getDiceCount() != dices[it.dice] ?: 0 }
    }

    fun toSingleDiceCollections(): List<SingleDiceCollection> {
        val result = mutableListOf<SingleDiceCollection>()
        for (value in DiceType.values()) {
            val dice = value.createDice()

            dices[dice]?.let {
                result.add(SingleDiceCollection(dice, it))
            }?.let { result.add(SingleDiceCollection(dice, 0)) }
        }
        return result
    }

    override fun hashCode(): Int = dices.hashCode()

    companion object {
        @JvmStatic
        fun createDiceCollectionFromDice(dice: Dice): DiceCollection {
            val diceCollection = DiceCollection()
            diceCollection.addDices(dice, 0)
            return diceCollection
        }

        @JvmStatic
        fun createDiceCollectionFromSingleDiceCollections(singleDiceCollections: List<SingleDiceCollection>): DiceCollection {
            val diceCollection = DiceCollection()
            singleDiceCollections
                    .filter { it.getDiceCount() != 0 }
                    .forEach { diceCollection.addDices(it.dice, it.getDiceCount()) }
            return diceCollection
        }

    }
}