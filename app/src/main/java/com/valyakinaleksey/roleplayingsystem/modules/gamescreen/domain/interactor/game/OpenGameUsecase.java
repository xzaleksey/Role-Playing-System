package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game;

import com.google.firebase.database.Transaction;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import rx.Observable;

public class OpenGameUsecase implements OpenGameInteractor {
  @Override public Observable<Boolean> openGame(GameModel gameModel) {
    return FireBaseUtils.setData(false, FireBaseUtils.getTableReference(FireBaseUtils.GAMES)
        .child(gameModel.getId())
        .child(GameModel.FIELD_FINISHED))
        .switchMap(aVoid -> FireBaseUtils.startTransaction(
            FireBaseUtils.getTableReference(FireBaseUtils.USERS).child(gameModel.getMasterId()),
            data -> {
              User user = data.getValue(User.class);
              if (user == null) {
                return Transaction.success(data);
              }
              user.setCountOfGamesMastered(user.getCountOfGamesMastered() - 1);
              data.setValue(user);
              return Transaction.success(data);
            }))
        .map(aVoid -> true);
  }
}