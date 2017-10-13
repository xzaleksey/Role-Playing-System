package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game

import android.support.v7.util.DiffUtil
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable
import rx.functions.Func2

class MyGamesUsecase() : MyGamesInteractor {
  private val myGamesIds: MutableSet<String> = mutableSetOf()

  private val gamesInUsersQuery: Query = FireBaseUtils.getTableReference(
      FireBaseUtils.GAMES_IN_USERS)
      .child(FireBaseUtils.getCurrentUserId())

  private val gamesQuery: Query = FireBaseUtils.getTableReference(FireBaseUtils.GAMES)

  override fun getMyGamesObservable(
      oldList: MutableList<IFlexible<*>>): Observable<List<IFlexible<*>>> {
    return Observable.combineLatest(getMyGameIds(), getMyGames(),
        Func2 { ids: List<String>, gameModels: Set<GameModel> ->
          val result: MutableList<IFlexible<*>> = mutableListOf()

          return@Func2 result.toList()
        }).onBackpressureLatest()
  }

  private fun getMyGameIds(): Observable<MutableList<String>> {
    return RxFirebaseDatabase.observeValueEvent(gamesInUsersQuery).map { dataSnaptshot ->
      val ids = mutableListOf<String>()
      if (dataSnaptshot.exists()) {
        dataSnaptshot.children.mapTo(ids) { it.getValue(GameModel::class.java)!!.id }
      }
      return@map ids
    }.startWith(mutableListOf<String>())
  }

  private fun getMyGames(): Observable<MutableSet<GameModel>> {
    return RxFirebaseDatabase.observeValueEvent(gamesQuery).map { dataSnaptshot ->
      val models = mutableSetOf<GameModel>()
      if (dataSnaptshot.exists()) {
        dataSnaptshot.children.mapTo(models) { it.getValue(GameModel::class.java)!! }
      }
      return@map models
    }.startWith(mutableSetOf<GameModel>())
  }
}


interface MyGamesInteractor {
  fun getMyGamesObservable(oldList: MutableList<IFlexible<*>>): Observable<List<IFlexible<*>>>
}