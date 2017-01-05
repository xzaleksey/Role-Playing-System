package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import org.joda.time.DateTime;

import rx.Observable;

public class CreateNewGameUseCase implements CreateNewGameInteractor {
    @Override
    public Observable<String> createNewGame(GameModel gameModel) {
        DatabaseReference games = FirebaseDatabase.getInstance().getReference().child(FireBaseUtils.GAMES);
        gameModel.setDateCreate(DateTime.now().getMillis());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            gameModel.setMasterId(currentUser.getUid());
            return RxFirebaseDatabase.getInstance().observeSetValuePush(games, gameModel)
                    .doOnNext(s -> {
                        games.child(s).child(FireBaseUtils.ID).setValue(s);
                        gameModel.setId(s);
                    });
        } else {
            throw new IllegalStateException("current user can't be null");
        }
    }
}
      