package com.valyakinaleksey.roleplayingsystem.data.repository.game.map

import com.kelvinapps.rxfirebase.RxFirebaseDatabase
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable
import rx.Observable
import rx.functions.Func1

class FirebaseFirebaseMapRepositoryImpl : FirebaseMapRepository {
  private fun query(gameId: String) = FireBaseUtils.getTableReference(
          FirebaseTable.GAME_MAPS).child(gameId)

  override fun observeMaps(gameId: String): Observable<Map<String, MapModel>> {
    val observeValueEvent = RxFirebaseDatabase.observeValueEvent<Map<String, MapModel>>(
        query(gameId),
        Func1 { datasnapshot ->
          val result: MutableMap<String, MapModel> = linkedMapOf()
          if (datasnapshot.exists()) {
            for (child in datasnapshot.children) {
              val value = child.getValue(MapModel::class.java)
              value!!.id = child.key
              value.gameId= gameId
              result.put(child.key, value)
            }
          }
          return@Func1 result
        })
    return observeValueEvent.onBackpressureLatest()
  }
}