package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.SimpleCrypto;

import rx.Observable;
import timber.log.Timber;

public class EditGameUseCase implements EditGameInteractor {

    private SimpleCrypto simpleCrypto;

    public EditGameUseCase(SimpleCrypto simpleCrypto) {
        this.simpleCrypto = simpleCrypto;
    }

    @Override
    public Observable<Object> saveField(GameModel gameModel, String fieldName, Object o) {
        return Observable.just(gameModel)
                .switchMap(gameModel1 -> {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference gamesName = reference.child(FireBaseUtils.GAMES).child(gameModel.getId()).child(fieldName);
                    gamesName.setValue(o);
                    return RxFirebaseDatabase.getInstance().observeSingleValue(gamesName).map(dataSnapshot -> {
                        Timber.d(dataSnapshot.toString());
                        return dataSnapshot.getValue();
                    });
                });

    }
}
      