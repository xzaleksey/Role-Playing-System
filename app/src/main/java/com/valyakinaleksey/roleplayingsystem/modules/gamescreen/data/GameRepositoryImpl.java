package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data;

import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import rx.Observable;
import rx.subjects.PublishSubject;

public class GameRepositoryImpl implements GameRepository {

    @Override
    public Observable<FirebaseChildEvent> observeGameChangedById(String id) {
        DatabaseReference databaseReference = getChildReference(id);
        return RxFirebaseDatabase.getInstance()
                .observeChildChanged(databaseReference);
    }

    @Override
    public Observable<Boolean> observeGameRemovedById(String id) {
        return RxFirebaseDatabase.getInstance().observeChildRemoved(getChildReference(id)).map(firebaseChildEvent -> {
            return true;
        });
    }

    private DatabaseReference getChildReference(String id) {
        return FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAMES)
                .child(id);
    }

    @Override
    public Observable<Boolean> deleteGame(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        PublishSubject<Boolean> booleanPublishSubject = PublishSubject.create();
        DatabaseReference childReference = getChildReference(id);
        DatabaseReference usersInGame = reference.child(FireBaseUtils.USERS_IN_GAME).child(id);
        DatabaseReference gamesInUser = reference.child(FireBaseUtils.GAMES_IN_USERS);
        childReference.removeValue();
        RxFirebaseDatabase.getInstance().observeSingleValue(usersInGame)
                .subscribe(dataSnapshot -> {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        UserInGameModel userInGameModel = data.getValue(UserInGameModel.class);
                        gamesInUser.child(userInGameModel.getUid()).child(id).removeValue();
                    }
                    usersInGame.removeValue();
                    childReference.removeValue();
                    booleanPublishSubject.onNext(true);
                }, throwable -> {
                    booleanPublishSubject.onError(throwable);
                    Crashlytics.logException(throwable);
                });

        return booleanPublishSubject;
    }
}
      