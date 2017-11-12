package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import com.google.firebase.database.DatabaseReference
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseGameRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils

class GameRacesRepositoryImpl() : AbstractFirebaseGameRepositoryImpl<GameRaceModel>(
        GameRaceModel::class.java), GameRacesRepository {

    override fun getDataBaseReference(gameId: String): DatabaseReference {
        return FireBaseUtils.getTableReference(FireBaseUtils.GAME_RACES).child(gameId)
    }

}

interface GameRacesRepository : FirebaseGameRepository<GameRaceModel>