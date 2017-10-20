package com.valyakinaleksey.roleplayingsystem.data.repository.game.map

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mapscreen.domain.model.MapModel
import rx.Observable

interface FirebaseMapRepository {
  fun observeMaps(gameId: String): Observable<Map<String, MapModel>>
}