package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor

import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollection
import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollectionRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceType
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable

class DiceInteractorImpl constructor(
        private val firebaseDiceCollectionRepository: FirebaseDiceCollectionRepository) : DiceInteractor {

    override fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection> {
        return firebaseDiceCollectionRepository.addData(gameId, FirebaseDiceCollection.newInstance(diceCollection))
                .map { it.mapToDiceCollection() }
    }

    override fun observeDiceCollections(gameId: String): Observable<List<DiceCollection>> {
        val result = mutableListOf<DiceCollection>()
        return firebaseDiceCollectionRepository.observeData(gameId).map {
            for (value in it.values) {
                result.add(value.mapToDiceCollection())
            }
            return@map result
        }
    }

    override fun getDefaultDicesModel(): MutableList<IFlexible<*>> {
        val result = mutableListOf<IFlexible<*>>()
        DiceType.values().mapTo(result) { DiceCollectionViewModel(it.resId, it.createSingleDiceCollection()) }
        return result
    }

}


interface DiceInteractor {
    fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection>

    fun observeDiceCollections(gameId: String): Observable<List<DiceCollection>>

    fun getDefaultDicesModel(): MutableList<IFlexible<*>>
}