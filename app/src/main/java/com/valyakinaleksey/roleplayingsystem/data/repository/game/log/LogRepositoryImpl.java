package com.valyakinaleksey.roleplayingsystem.data.repository.game.log;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseGameRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import rx.Observable;

public class LogRepositoryImpl extends AbstractFirebaseGameRepositoryImpl<MasterLogMessage> implements LogRepository {

    public LogRepositoryImpl() {
        super(MasterLogMessage.class);
    }


    @Override
    public Observable<MasterLogMessage> sendMessage(String gameId, MasterLogMessage masterLogMessage) {
        DatabaseReference reference = getDataBaseReference(gameId);
        masterLogMessage.setTempDateCreate(DateTime.now().getMillis());
        return FireBaseUtils.observeSetValuePush(reference, masterLogMessage).map(aVoid -> masterLogMessage);
    }

    @NotNull
    @Override
    public DatabaseReference getDataBaseReference(@NonNull String gameId) {
        return FireBaseUtils.getTableReference(FirebaseTable.GAME_LOG).child(gameId);
    }

}
      