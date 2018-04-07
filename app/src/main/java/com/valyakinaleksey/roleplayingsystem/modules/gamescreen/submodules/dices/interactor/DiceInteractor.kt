package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.interactor

import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.repository.StringRepository
import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollection
import com.valyakinaleksey.roleplayingsystem.data.repository.game.dices.FirebaseDiceCollectionRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.collectionadapter.DiceCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceadapter.DiceSingleCollectionViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter.DiceTotalResultViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter.SingleDiceTypeResultViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.model.*
import com.valyakinaleksey.roleplayingsystem.utils.extensions.convertDpToPixel
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Completable
import rx.Observable

class DiceInteractorImpl constructor(
        private val firebaseDiceCollectionRepository: FirebaseDiceCollectionRepository,
        private val stringRepository: StringRepository) : DiceInteractor {

    override fun getDefaultSingleDicesCollections(): List<SingleDiceCollection> {
        return DiceType.values().map { SingleDiceCollection(it.createDice(), 0) }
    }

    override fun removeDiceCollection(gameId: String, diceCollection: DiceCollection): Completable {
        return firebaseDiceCollectionRepository.removeData(gameId, FirebaseDiceCollection.newInstance(diceCollection))
    }

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

    override fun mapDiceResult(diceCollectionResult: DiceCollectionResult): MutableList<IFlexible<*>> {
        val result = mutableListOf<IFlexible<*>>()
        val currentResult = diceCollectionResult.getCurrentResult()
        val maxResult = diceCollectionResult.getMaxResult()
        result.add(DiceTotalResultViewModel(currentResult.toString(),
                maxResult.toString() + " (${stringRepository.getMax()})"))
        result.add(SubHeaderViewModel.Builder()
                .paddingLeft(8.convertDpToPixel())
                .title(stringRepository.getDetails())
                .build()
        )
        for (diceResultEntry in diceCollectionResult.getDiceResults()) {
            val diceType = DiceType.getDiceType(diceResultEntry.key)
            val diceResults = diceResultEntry.value
            result.add(SingleDiceTypeResultViewModel(diceResults,
                    diceResults.sumBy { it.value }, diceType, false))
        }
        return result
    }

    override fun mapSingleDiceCollectionsToDicesModel(diceCollections: List<SingleDiceCollection>): List<DiceSingleCollectionViewModel> {
        return diceCollections.map {
            DiceSingleCollectionViewModel(DiceType.getDiceType(it.dice).resId, it)
        }
    }
}


interface DiceInteractor {
    fun addDiceCollection(gameId: String, diceCollection: DiceCollection): Observable<DiceCollection>

    fun observeDiceCollections(gameId: String): Observable<List<DiceCollection>>

    fun mapDiceCollections(diceViewModel: DiceViewModel): MutableList<IFlexible<*>>

    fun getDefaultSingleDicesCollections(): List<SingleDiceCollection>

    fun mapSingleDiceCollectionsToDicesModel(diceCollections: List<SingleDiceCollection>): List<IFlexible<*>>

    fun removeDiceCollection(gameId: String, diceCollection: DiceCollection): Completable

    fun mapDiceResult(diceCollectionResult: DiceCollectionResult): MutableList<IFlexible<*>>
}