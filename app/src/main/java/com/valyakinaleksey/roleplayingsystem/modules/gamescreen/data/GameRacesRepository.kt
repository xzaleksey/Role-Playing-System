package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import com.google.firebase.database.DatabaseReference
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils

class GameRacesRepositoryImpl(
    private val gameId: String) : AbstractFirebaseRepositoryImpl<GameRaceModel>(
    GameRaceModel::class.java), GameRacesRepository {

  override fun getDataBaseReference(): DatabaseReference {
    return FireBaseUtils.getTableReference(FireBaseUtils.GAME_RACES).child(gameId)
  }

}

interface GameRacesRepository : FirebaseRepository<GameRaceModel>