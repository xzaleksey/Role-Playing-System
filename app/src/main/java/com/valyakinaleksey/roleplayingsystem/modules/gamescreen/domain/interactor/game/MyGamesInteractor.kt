package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game

import com.google.firebase.database.Query
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.TwoLineWithIdViewModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.utils.DateFormats
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import eu.davidea.flexibleadapter.items.IFlexible
import org.joda.time.DateTime
import rx.Observable
import rx.functions.Func2

class MyGamesUsecase : MyGamesInteractor {
  private val gamesInUsersQuery: Query = FireBaseUtils.getTableReference(
      FireBaseUtils.GAMES_IN_USERS)
      .child(FireBaseUtils.getCurrentUserId())
  private val gamesQuery: Query = FireBaseUtils.getTableReference(FireBaseUtils.GAMES)

  override fun getMyGamesObservable(): Observable<MutableList<IFlexible<*>>> {
    return Observable.combineLatest(getMyGameIds(), getMyGames(),
        Func2 { ids: List<String>, gameModels: Map<String, GameModel> ->
          val currentUserId = FireBaseUtils.getCurrentUserId()
          val myGames: MutableList<GameModel> = mutableListOf()
          val myMasterGames: MutableList<GameModel> = mutableListOf()
          for (id in ids) {
            gameModels[id]?.let { gameModel ->
              if (gameModel.masterId == currentUserId) {
                myMasterGames.add(gameModel)
              } else {
                myGames.add(gameModel)
              }
            }
          }


          return@Func2 getFilledModel(myMasterGames, myGames)
        }).onBackpressureLatest()
  }

  private fun getFilledModel(myMasterGames: MutableList<GameModel>,
      myGames: MutableList<GameModel>): MutableList<IFlexible<*>> {
    val result = mutableListOf<IFlexible<*>>()

    if (myMasterGames.isNotEmpty()) {
      result.add(SubHeaderViewModel(StringUtils.getStringById(R.string.continue_lead_the_game)))
      myMasterGames.mapTo(result) {
        TwoLineWithIdViewModel(it.id, it.name, getMasterSecondaryText(it))
      }
    }
    if (myGames.isNotEmpty()) {
      result.add(SubHeaderViewModel(StringUtils.getStringById(R.string.continue_play_the_game)))
      myMasterGames.mapTo(result) {
        TwoLineWithIdViewModel(it.id, it.name, getSecondaryText(it))
      }
    }
    return result
  }

  private fun getSecondaryText(gameModel: GameModel): String {
    return "${StringUtils.getStringById(R.string.master)} ${gameModel.masterName}"
  }

  private fun getMasterSecondaryText(
      myMasterGame: GameModel): String? {
    return StringUtils.getStringById(R.string.started) + " " + DateTime(
        myMasterGame.dateCreateLong).toString(DateFormats.dayMonthFull)
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

  private fun getMyGames(): Observable<MutableMap<String, GameModel>> {
    return RxFirebaseDatabase.observeValueEvent(gamesQuery).map { dataSnaptshot ->
      val models = mutableMapOf<String, GameModel>()
      if (dataSnaptshot.exists()) {
        dataSnaptshot.children
            .map { it.getValue(GameModel::class.java)!! }
            .forEach { models.put(it.id, it) }
      }
      return@map models
    }.startWith(mutableMapOf())
  }
}


interface MyGamesInteractor {
  fun getMyGamesObservable(): Observable<MutableList<IFlexible<*>>>
}