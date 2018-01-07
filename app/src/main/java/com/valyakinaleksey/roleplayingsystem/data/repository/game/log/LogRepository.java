package com.valyakinaleksey.roleplayingsystem.data.repository.game.log;

import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseGameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;

import rx.Observable;

public interface LogRepository extends FirebaseGameRepository<MasterLogMessage> {

    Observable<MasterLogMessage> sendMessage(String gameId, MasterLogMessage masterLogMessage);
}
      