package com.valyakinaleksey.roleplayingsystem.data.repository.game.diceresults

import com.google.firebase.database.Exclude
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.Dice
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollectionResult
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceResult
import java.util.*

class FirebaseDiceResultCollection : HasId {
    var dicesResults: MutableMap<String, List<Int>> = HashMap()
    private var id: String? = null

    override fun getId(): String? {
        return id
    }

    override fun setId(id: String) {
        this.id = id
    }

    @Exclude
    fun mapToDiceCollection(): DiceCollectionResult {
        val diceCollectionResult = DiceCollectionResult(id)
        for (dicesResultEntry in dicesResults) {
            val diceValue = dicesResultEntry.key.toInt()
            val dice = Dice(diceValue)
            for (diceResult in dicesResultEntry.value) {
                diceCollectionResult.addDiceResult(DiceResult(dice, diceResult))
            }
        }

        return diceCollectionResult
    }

    companion object {

        fun newInstance(diceCollection: DiceCollectionResult): FirebaseDiceResultCollection {
            val firebaseDiceResultCollection = FirebaseDiceResultCollection()
            firebaseDiceResultCollection.id = diceCollection.id
            for (diceResultEntry in diceCollection.getDiceResults()) {
                val result = mutableListOf<Int>()
                firebaseDiceResultCollection.dicesResults[diceResultEntry.key.maxValue.toString()] = result
                for (diceResult in diceResultEntry.value) {
                    result.add(diceResult.value)
                }
            }

            return firebaseDiceResultCollection
        }
    }
}
