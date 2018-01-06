package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Transaction;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class LeaveGameUseCase implements LeaveGameInteractor {
    @Override
    public Observable<Void> leaveGame(GameModel gameModel) {
        String currentUserId = FireBaseUtils.getCurrentUserId();
        DatabaseReference gameCharactersReference =
                FireBaseUtils.getTableReference(FirebaseTable.GAME_CHARACTERS).child(gameModel.getId());
        return RxFirebaseDatabase.observeSingleValueEvent(gameCharactersReference)
                .switchMap(dataSnapshot -> {
                    List<GameCharacterModel> gameCharacterModels = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GameCharacterModel value = snapshot.getValue(GameCharacterModel.class);
                        if (value != null && currentUserId.equals(value.getUid())) {
                            gameCharacterModels.add(value);
                        }
                    }
                    return Observable.from(gameCharacterModels);
                })
                .flatMap(gameCharacterModel -> FireBaseUtils.startTransaction(gameCharactersReference.child(gameCharacterModel.getId()),
                        GameCharacterModel.class,
                        gameCharacterModel1 -> gameCharacterModel1.setUid(null)))
                .toList()
                .switchMap(dataSnapshot -> FireBaseUtils.setData(null,
                        FireBaseUtils.getTableReference(FirebaseTable.USERS_IN_GAME)
                                .child(gameModel.getId())
                                .child(currentUserId)))
                .switchMap(aVoid -> FireBaseUtils.setData(null,
                        FireBaseUtils.getTableReference(FirebaseTable.GAMES_IN_USERS)
                                .child(currentUserId)
                                .child(gameModel.getId())))
                .switchMap(aVoid -> FireBaseUtils.setData(null, FireBaseUtils.getTableReference(FirebaseTable.CHARACTERS_IN_USER)
                        .child(currentUserId)
                        .child(gameModel.getId()))
                )
                .switchMap(aVoid -> FireBaseUtils.startTransaction(
                        FireBaseUtils.getTableReference(FirebaseTable.USERS).child(currentUserId), data -> {
                            User user = data.getValue(User.class);
                            if (user == null) {
                                return Transaction.success(data);
                            }
                            user.setCountOfGamesPlayed(user.getCountOfGamesPlayed() - 1);
                            data.setValue(user);
                            return Transaction.success(data);
                        }));
    }
}
      