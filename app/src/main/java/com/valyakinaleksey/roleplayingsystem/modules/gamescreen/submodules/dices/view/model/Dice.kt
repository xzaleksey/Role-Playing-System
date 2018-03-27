package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model

import java.io.Serializable

class Dice(val maxValue: Int) : Comparable<Dice>, Serializable {

    fun getRndValue(): Int {
        return 1 + (maxValue * Math.random()).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Dice

        if (maxValue != other.maxValue) return false

        return true
    }

    override fun hashCode(): Int {
        return maxValue
    }

    override fun compareTo(other: Dice): Int {
        return this.maxValue - other.maxValue
    }

}