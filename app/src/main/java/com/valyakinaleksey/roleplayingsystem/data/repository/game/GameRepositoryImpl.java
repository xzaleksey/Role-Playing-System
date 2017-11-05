package com.valyakinaleksey.roleplayingsystem.data.repository.game;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.UserInGameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import org.jetbrains.annotations.NotNull;
import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.*;

public class GameRepositoryImpl extends AbstractFirebaseRepositoryImpl<GameModel>
        implements GameGameRepository {

    private Subscription subscription = Subscriptions.unsubscribed();
    private Map<String, GameModel> gamesMap = Collections.emptyMap();
    private BehaviorSubject<Map<String, GameModel>> subject = BehaviorSubject.create(gamesMap);

    public GameRepositoryImpl() {
        super(GameModel.class);
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                subscription.unsubscribe();
            } else {
                initSubscription();
            }
        });
    }

    private void initSubscription() {
        subscription = super.observeData()
                .throttleLast(50, TimeUnit.MILLISECONDS)
                .compose(RxTransformers.applyIoSchedulers())
                .onBackpressureLatest()
                .subscribe(gameModelMap -> {
                    synchronized (this) {
                        gamesMap = gameModelMap;
                        subject.onNext(gameModelMap);
                    }
                }, throwable -> Timber.e(throwable, "games error"));
    }

    @Override
    public Observable<Map<String, GameModel>> observeData() {
        return subject;
    }

    @Override
    public synchronized GameModel getGameModelById(String id) {
        return gamesMap.get(id);
    }

    @Override
    public Observable<GameModel> getGameModelObservableById(String id) {
        GameModel gameModel = gamesMap.get(id);
        if (gameModel != null) {
            return Observable.just(gameModel);
        }
        return RxFirebaseDatabase.observeSingleValueEvent(getDataBaseReference().child(id),
                GameModel.class);
    }

    @Override
    public Observable<RxFirebaseChildEvent<DataSnapshot>> observeGameChangedById(String id) {
        DatabaseReference databaseReference = getChildReference(id);
        return FireBaseUtils.observeChildChanged(databaseReference);
    }

    @Override
    public Observable<Boolean> observeGameRemovedById(String id) {
        return FireBaseUtils.observeChildRemoved(getChildReference(id)).map(firebaseChildEvent -> true);
    }

    private DatabaseReference getChildReference(String id) {
        return FirebaseDatabase.getInstance().getReference().child(GAMES).child(id);
    }

    @Override
    public Observable<Boolean> deleteGame(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        PublishSubject<Boolean> booleanPublishSubject = PublishSubject.create();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference childReference = getChildReference(id);
        DatabaseReference usersInGame = reference.child(USERS_IN_GAME).child(id);
        Map<String, Object> childUpdates = new HashMap<>();
        childReference.removeValue();
        com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeSingleValueEvent(usersInGame)
                .subscribe(dataSnapshot -> {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        UserInGameModel userInGameModel = data.getValue(UserInGameModel.class);
                        childUpdates.put(
                                String.format(FORMAT_SLASHES, GAMES_IN_USERS) + userInGameModel.getUid() + "/" + id,
                                null);
                        childUpdates.put(
                                String.format(FORMAT_SLASHES, CHARACTERS_IN_USER) + userInGameModel.getUid() + "/" + id,
                                null);
                    }
                    childUpdates.put(String.format(FORMAT_SLASHES, USERS_IN_GAME) + id, null);
                    childUpdates.put(String.format(FORMAT_SLASHES, GAME_CHARACTERISTICS) + id, null);
                    childUpdates.put(String.format(FORMAT_SLASHES, GAME_CLASSES) + id, null);
                    childUpdates.put(String.format(FORMAT_SLASHES, GAME_RACES) + id, null);
                    childUpdates.put(String.format(FORMAT_SLASHES, GAME_LOG) + id, null);
                    childUpdates.put(String.format(FORMAT_SLASHES, GAME_CHARACTERS) + id, null);
                    databaseReference.updateChildren(childUpdates);
                    booleanPublishSubject.onNext(true);
                }, throwable -> {
                    booleanPublishSubject.onError(throwable);
                    Crashlytics.logException(throwable);
                });

        return booleanPublishSubject;
    }

    @NotNull
    @Override
    public DatabaseReference getDataBaseReference() {
        return FireBaseUtils.getTableReference(FireBaseUtils.GAMES);
    }
}
      