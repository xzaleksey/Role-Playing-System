package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game

import com.google.firebase.database.Query
import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.R.string
import com.valyakinaleksey.roleplayingsystem.core.flexible.CommonDividerViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.ShadowDividerViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel
import com.valyakinaleksey.roleplayingsystem.core.flexible.TwoLineWithIdViewModel
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.utils.DateFormats
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import eu.davidea.flexibleadapter.items.IFlexible
import org.joda.time.DateTime
import rx.Observable
import rx.functions.Func2

class MyGamesUsecase(private val gamesRepository: GameRepository) : MyGamesInteractor {
  private val gamesInUsersQuery: Query = FireBaseUtils.getTableReference(
      FireBaseUtils.GAMES_IN_USERS)
      .child(FireBaseUtils.getCurrentUserId())

  override fun getMyGamesObservable(): Observable<MutableList<IFlexible<*>>> {
    return Observable.combineLatest(getMyGameIds(), getMyGames(),
        Func2 { ids: List<String>, gameModels: Map<String, GameModel> ->
          val currentUserId = FireBaseUtils.getCurrentUserId()
          val myGames: MutableList<GameModel> = mutableListOf()
          val myMasterGames: MutableList<GameModel> = mutableListOf()
          val finishedGames: MutableList<GameModel> = mutableListOf()
          for (id in ids) {
            gameModels[id]?.let { gameModel ->
              if (gameModel.isFinished) {
                finishedGames.add(gameModel)
              } else if (gameModel.masterId == currentUserId) {
                myMasterGames.add(gameModel)
              } else {
                myGames.add(gameModel)
              }
            }
          }


          return@Func2 getFilledModel(myMasterGames, myGames, finishedGames)
        }).onBackpressureLatest()
  }

  private fun getFilledModel(
      myMasterGames: MutableList<GameModel>,
      myGames: MutableList<GameModel>,
      finishedGames: MutableList<GameModel>): MutableList<IFlexible<*>> {
    val result = mutableListOf<IFlexible<*>>()
    fillMasterGames(myMasterGames, result)
    fillMyGames(myGames, result)
    fillFinishedGames(finishedGames, result)
    return result
  }

  private fun fillMasterGames(
      myMasterGames: MutableList<GameModel>,
      result: MutableList<IFlexible<*>>) {
    if (myMasterGames.isNotEmpty()) {
      result.add(SubHeaderViewModel(StringUtils.getStringById(string.continue_lead_the_game)))
      for ((index, myMasterGame) in myMasterGames.withIndex()) {
        result.add(TwoLineWithIdViewModel(myMasterGame.id, myMasterGame.name,
            getMasterSecondaryText(myMasterGame)))
        addDivider(index, myMasterGames, result)
      }
    }
  }

  private fun fillMyGames(
      myGames: MutableList<GameModel>,
      result: MutableList<IFlexible<*>>) {
    if (myGames.isNotEmpty()) {
      result.add(SubHeaderViewModel(StringUtils.getStringById(string.continue_play_the_game)))
      for ((index, myGame) in myGames.withIndex()) {
        result.add(TwoLineWithIdViewModel(myGame.id, myGame.name,
            getSecondaryText(myGame)))
        addDivider(index, myGames, result)
      }
    }
  }

  private fun fillFinishedGames(finishedGames: MutableList<GameModel>,
      result: MutableList<IFlexible<*>>) {
    if (finishedGames.isNotEmpty()) {
      result.add(SubHeaderViewModel(StringUtils.getStringById(string.completed_games)))
      for ((index, myMasterGame) in finishedGames.withIndex()) {
        result.add(TwoLineWithIdViewModel(myMasterGame.id, myMasterGame.name,
            getFinishedSecondaryText(myMasterGame)))
        addDivider(index, finishedGames, result)
      }
    }
  }

  private fun addDivider(index: Int,
      games: MutableList<GameModel>,
      result: MutableList<IFlexible<*>>) {
    val divider: IFlexible<*> = if (index != games.lastIndex) {
      CommonDividerViewModel(result.size)
    } else {
      ShadowDividerViewModel(result.size)
    }
    result.add(divider)
  }

  private fun getSecondaryText(gameModel: GameModel): String {
    return "${StringUtils.getStringById(R.string.master)} ${gameModel.masterName}"
  }

  private fun getMasterSecondaryText(
      myMasterGame: GameModel): String {
    return StringUtils.getStringById(R.string.started) + " " + DateTime(
        myMasterGame.dateCreateLong).toString(DateFormats.dayMonthFull)
  }

  private fun getFinishedSecondaryText(
      gameModel: GameModel): String {
    return getMasterSecondaryText(gameModel)
  }

  private fun getMyGameIds(): Observable<MutableList<String>> {
    return RxFirebaseDatabase.observeValueEvent(gamesInUsersQuery).map { dataSnaptshot ->
      val ids = mutableListOf<String>()
      if (dataSnaptshot.exists()) {
        dataSnaptshot.children.mapTo(ids) { it.getValue(GameModel::class.java)!!.id }
      }
      return@map ids
    }
  }

  private fun getMyGames(): Observable<MutableMap<String, GameModel>> =
      gamesRepository.observeData()
}


interface MyGamesInteractor {
  fun getMyGamesObservable(): Observable<MutableList<IFlexible<*>>>
}