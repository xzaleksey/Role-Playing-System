package com.valyakinaleksey.roleplayingsystem.data.repository.game;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import rx.Observable;

import java.util.Map;

public interface GameRepository extends FirebaseRepository<GameModel> {

    GameModel getGameModelById(String id);

    Observable<GameModel> getGameModelObservableById(String id);

    Observable<RxFirebaseChildEvent<DataSnapshot>> observeGameChangedById(String id);

    Observable<Boolean> observeGameRemovedById(String id);

    Observable<Boolean> deleteGame(String id);

    Observable<Map<String, GameModel>> getLastGamesModelByUserId(String userId, int lastGamesCount);

    Observable<Void> updateLastVisit(String id);
}
      