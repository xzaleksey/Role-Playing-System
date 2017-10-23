package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import com.google.firebase.database.DatabaseReference
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils


class GameClassesRepositoryImpl(
    private val gameId: String) : AbstractFirebaseRepositoryImpl<GameClassModel>(
    GameClassModel::class.java), GameClassesRepository {

  override fun getDataBaseReference(): DatabaseReference {
    return FireBaseUtils.getTableReference(FireBaseUtils.GAME_CLASSES).child(gameId)
  }

}

interface GameClassesRepository : FirebaseRepository<GameClassModel>