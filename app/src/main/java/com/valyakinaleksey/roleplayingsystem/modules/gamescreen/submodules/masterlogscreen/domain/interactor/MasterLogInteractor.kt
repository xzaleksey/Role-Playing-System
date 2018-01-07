package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor

import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable

interface MasterLogInteractor {
    fun sendMessage(gameModel: GameModel, masterLogMessage: MasterLogMessage): Observable<MasterLogMessage>
    fun observeMessages(gameId: String): Observable<List<IFlexible<*>>>
}
      