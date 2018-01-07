package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor

import com.valyakinaleksey.roleplayingsystem.data.repository.game.log.LogRepository
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessageViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import rx.Observable

class MasterLogUseCase(private val logRepository: LogRepository) : MasterLogInteractor {

    override fun sendMessage(gameModel: GameModel, masterLogMessage: MasterLogMessage): Observable<MasterLogMessage> {
        return logRepository.sendMessage(gameModel.id, masterLogMessage)
    }

    override fun observeMessages(gameId: String): Observable<List<IFlexible<*>>> {
        return logRepository.observeData(gameId)
                .map { messages ->
                    val result = ArrayList<IFlexible<*>>()
                    messages.values.reversed().forEach { message ->
                        result.add(MasterLogMessageViewModel(message))
                    }

                    return@map result
                }
    }

}
      