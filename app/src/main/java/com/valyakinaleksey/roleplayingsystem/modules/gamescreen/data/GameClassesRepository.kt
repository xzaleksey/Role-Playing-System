package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data

import com.google.firebase.database.DatabaseReference
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseGameRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils


class ClassesRepositoryImpl() : AbstractFirebaseGameRepositoryImpl<GameClassModel>(
        GameClassModel::class.java), GameClassesRepository {

    override fun getDataBaseReference(gameId: String): DatabaseReference {
        return FireBaseUtils.getTableReference(FireBaseUtils.GAME_CLASSES).child(gameId)
    }

}

interface GameClassesRepository : FirebaseGameRepository<GameClassModel>