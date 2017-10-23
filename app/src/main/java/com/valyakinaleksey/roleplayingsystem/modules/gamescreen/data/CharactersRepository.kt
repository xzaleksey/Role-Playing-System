package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import com.google.firebase.database.DatabaseReference
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils

class CharactersRepositoryImpl(
    private val gameId: String) : AbstractFirebaseRepositoryImpl<GameCharacterModel>(
    GameCharacterModel::class.java), CharactersRepository {

  override fun getDataBaseReference(): DatabaseReference {
    return FireBaseUtils.getTableReference(FireBaseUtils.GAME_CHARACTERS).child(gameId)
  }

}

interface CharactersRepository : FirebaseRepository<GameCharacterModel> {
}