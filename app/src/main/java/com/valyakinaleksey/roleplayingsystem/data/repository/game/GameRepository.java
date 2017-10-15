package com.valyakinaleksey.roleplayingsystem.data.repository.game;

import com.google.firebase.database.DataSnapshot;

import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import java.util.Map;
import rx.Observable;

public interface GameRepository {

  Observable<Map<String, GameModel>> observeGames();

  GameModel getGameModelById(String id);

  Observable<RxFirebaseChildEvent<DataSnapshot>> observeGameChangedById(String id);

  Observable<Boolean> observeGameRemovedById(String id);

  Observable<Boolean> deleteGame(String id);
}
      