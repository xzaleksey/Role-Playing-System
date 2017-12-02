package com.valyakinaleksey.roleplayingsystem.data.repository.game;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AbstractFirebaseRepositoryImpl;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameInUserModel;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameInUserModel.FIELD_LAST_VISITED_DATE;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.*;

public class GameRepositoryImpl extends AbstractFirebaseRepositoryImpl<GameModel>
        implements GameRepository {

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
        return subject.distinctUntilChanged();
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
        DatabaseReference usersInGame = reference.child(USERS_IN_GAME).child(id);
        com.kelvinapps.rxfirebase.RxFirebaseDatabase.observeSingleValueEvent(usersInGame)
                .subscribe(dataSnapshot -> {
                    Map<String, Object> childUpdates = new HashMap<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        UserInGameModel userInGameModel = data.getValue(UserInGameModel.class);
                        String uid = userInGameModel.getUid();
                        childUpdates.put(
                                String.format(FORMAT_SLASHES, GAMES_IN_USERS) + uid + "/" + id, null);
                        childUpdates.put(
                                String.format(FORMAT_SLASHES, CHARACTERS_IN_USER) + uid + "/" + id, null);
                    }
                    childUpdates.put(String.format(FORMAT_SLASHES, GAMES) + id, null);
                    childUpdates.put(String.format(FORMAT_SLASHES, GAMES_IN_USERS) + getCurrentUserId() + "/" + id, null);
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

    @Override
    public Observable<Map<String, GameModel>> getLastGamesModelByUserId(String userId, int lastGamesCount) {
        DatabaseReference databaseReference = FireBaseUtils.getTableReference(GAMES_IN_USERS).child(userId);
        return Observable.combineLatest(observeData(), RxFirebaseDatabase.observeValueEvent(databaseReference), (stringGameModelMap, dataSnapshot) -> {
            Map<Long, String> gameInUserModelsIds = new TreeMap<>();
            LinkedHashMap<String, GameModel> gameModels = new LinkedHashMap<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GameInUserModel gameInUserModel = snapshot.getValue(GameInUserModel.class);
                    String key = snapshot.getKey();
                    gameInUserModelsIds.put(gameInUserModel.getLastVisitedLong(), key);
                }
            }
            if (!gameInUserModelsIds.isEmpty()) {
                int counter = 0;
                int index;
                ArrayList<String> ids = new ArrayList<>(gameInUserModelsIds.values());
                while (counter != lastGamesCount && (index = (ids.size() - 1 - counter)) >= 0) {
                    String key = ids.get(index);
                    GameModel value = stringGameModelMap.get(key);
                    if (value != null) {
                        gameModels.put(key, value);
                    }
                    counter++;
                }
            }

            return gameModels;
        });
    }

    @Override
    public Observable<Map<String, GameModel>> getGamesByUserId(String userId) {
        DatabaseReference databaseReference = FireBaseUtils.getTableReference(GAMES_IN_USERS).child(userId);
        return Observable.combineLatest(observeData(), RxFirebaseDatabase.observeValueEvent(databaseReference), (stringGameModelMap, dataSnapshot) -> {
            Map<Long, String> gameInUserModelsIds = new TreeMap<>();
            LinkedHashMap<String, GameModel> gameModels = new LinkedHashMap<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GameInUserModel gameInUserModel = snapshot.getValue(GameInUserModel.class);
                    String key = snapshot.getKey();
                    gameInUserModelsIds.put(gameInUserModel.getLastVisitedLong(), key);
                }
            }
            if (!gameInUserModelsIds.isEmpty()) {
                ArrayList<String> ids = new ArrayList<>(gameInUserModelsIds.values());
                for (String id : ids) {
                    GameModel value = stringGameModelMap.get(id);
                    if (value != null) {
                        gameModels.put(id, value);
                    }
                }
            }
            return gameModels;
        });
    }

    @Override
    public Observable<Void> updateLastVisit(String id) {
        return FireBaseUtils.setData(ServerValue.TIMESTAMP, FireBaseUtils.getTableReference(GAMES_IN_USERS)
                .child(FireBaseUtils.getCurrentUserId())
                .child(id)
                .child(FIELD_LAST_VISITED_DATE));
    }

    @NotNull
    @Override
    public DatabaseReference getDataBaseReference() {
        return FireBaseUtils.getTableReference(FireBaseUtils.GAMES);
    }
}
      