package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor

import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollection
import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollectionRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import timber.log.Timber

class DiceInteractorImpl constructor(
        private val firebaseDiceCollectionRepository: FirebaseDiceCollectionRepository) : DiceInteractor {

    override fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection> {
        return firebaseDiceCollectionRepository.addData(gameId, FirebaseDiceCollection.newInstance(diceCollection))
                .map { it.mapToDiceCollection() }
    }

    override fun observeDiceCollections(gameId: String): Observable<List<IFlexible<*>>> {
        return firebaseDiceCollectionRepository.observeData(gameId).map {
            for (value in it.values) {
                Timber.d(value.id)
                for (dice in value.dices) {
                    Timber.d("${dice.key}  ${dice.value}")
                }
            }
            return@map emptyList<IFlexible<*>>()
        }
    }
}


interface DiceInteractor {
    fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection>

    fun observeDiceCollections(gameId: String): Observable<List<IFlexible<*>>>
}