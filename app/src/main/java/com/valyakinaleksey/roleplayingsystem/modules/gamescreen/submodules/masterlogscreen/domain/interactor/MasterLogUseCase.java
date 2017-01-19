package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.masterlogscreen.domain.model.MasterLogMessage;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import rx.Observable;

public class MasterLogUseCase implements MasterLogInteractor {

    @Override
    public Observable<MasterLogMessage> sendMessage(GameModel gameModel, MasterLogMessage masterLogMessage) {
        DatabaseReference reference = getReference(gameModel);
        return RxFirebaseDatabase.getInstance().observeSetValuePush(reference, masterLogMessage)
                .map(s -> {
                    reference.child(s).child(FireBaseUtils.ID).setValue(s);
                    masterLogMessage.setId(s);
                    return masterLogMessage;
                });
    }

    @Override
    public Observable<Boolean> checkLogExists(GameModel gameModel) {
        return RxFirebaseDatabase.getInstance().observeSingleValue(getReference(gameModel))
                .map(DataSnapshot::exists);
    }

    private DatabaseReference getReference(GameModel gameModel) {
        return FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAME_LOG).child(gameModel.getId());
    }
}
      