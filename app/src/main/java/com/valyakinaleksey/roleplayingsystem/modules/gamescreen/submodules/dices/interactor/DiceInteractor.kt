package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor

import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollection
import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollectionRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.collectionadapter.DiceCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceSingleCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceCollection
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceType
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.DiceViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable

class DiceInteractorImpl constructor(
        private val firebaseDiceCollectionRepository: FirebaseDiceCollectionRepository) : DiceInteractor {

    override fun mapDiceCollections(diceViewModel: DiceViewModel): MutableList<IFlexible<*>> {
        val selectedDiceCollection = diceViewModel.selectedDiceCollection
        val savedDiceCollections = diceViewModel.savedDiceCollections

        val result = mutableListOf<IFlexible<*>>()
        for (savedDiceCollection in savedDiceCollections) {
            result.add(DiceCollectionViewModel(savedDiceCollection, savedDiceCollection == selectedDiceCollection))
        }
        return result
    }

    override fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection> {
        return firebaseDiceCollectionRepository.addData(gameId, FirebaseDiceCollection.newInstance(diceCollection))
                .map { it.mapToDiceCollection() }
    }

    override fun observeDiceCollections(gameId: String): Observable<List<DiceCollection>> {
        return firebaseDiceCollectionRepository.observeData(gameId).map {
            val result = mutableListOf<DiceCollection>()

            for (value in it.values) {
                result.add(value.mapToDiceCollection())
            }
            return@map result
        }
    }

    override fun getDefaultDicesModel(): MutableList<IFlexible<*>> {
        val result = mutableListOf<IFlexible<*>>()
        DiceType.values().mapTo(result) { DiceSingleCollectionViewModel(it.resId, it.createSingleDiceCollection()) }
        return result
    }

    override fun mapDicesCollectionToDicesModel(diceCollection: DiceCollection): List<DiceSingleCollectionViewModel> {
        return diceCollection.toSingleDiceCollections().map {
            DiceSingleCollectionViewModel(DiceType.getDiceType(it.dice).resId, it)
        }
    }
}


interface DiceInteractor {
    fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection>

    fun observeDiceCollections(gameId: String): Observable<List<DiceCollection>>

    fun mapDiceCollections(diceViewModel: DiceViewModel): MutableList<IFlexible<*>>

    fun getDefaultDicesModel(): MutableList<IFlexible<*>>

    fun mapDicesCollectionToDicesModel(diceCollection: DiceCollection): List<IFlexible<*>>
}