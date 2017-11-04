package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Transaction;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.*;

public class LeaveGameUseCase implements LeaveGameInteractor {
    @Override
    public Observable<Void> leaveGame(GameModel gameModel) {
        String currentUserId = FireBaseUtils.getCurrentUserId();
        DatabaseReference gameCharactersReference =
                FireBaseUtils.getTableReference(GAME_CHARACTERS).child(gameModel.getId());
        return RxFirebaseDatabase.observeSingleValueEvent(gameCharactersReference)
                .doOnNext(dataSnapshot -> {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GameCharacterModel value = snapshot.getValue(GameCharacterModel.class);
                        if (currentUserId.equals(value.getUid())) {
                            gameCharactersReference.child(value.getId()).child(FireBaseUtils.UID).setValue(null);
                        }
                    }
                })
                .switchMap(dataSnapshot -> FireBaseUtils.setData(null,
                        FireBaseUtils.getTableReference(USERS_IN_GAME)
                                .child(gameModel.getId())
                                .child(currentUserId)))
                .switchMap(aVoid -> FireBaseUtils.setData(null,
                        FireBaseUtils.getTableReference(GAMES_IN_USERS)
                                .child(currentUserId)
                                .child(gameModel.getId())))
                .switchMap(aVoid -> FireBaseUtils.setData(null, FireBaseUtils.getTableReference(CHARACTERS_IN_USER)
                        .child(currentUserId)
                        .child(gameModel.getId()))
                )
                .switchMap(aVoid -> FireBaseUtils.startTransaction(
                        FireBaseUtils.getTableReference(USERS).child(currentUserId), data -> {
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
      