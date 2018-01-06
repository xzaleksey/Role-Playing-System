package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes;

import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.abstractions.BaseGameTEditInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;

import static com.valyakinaleksey.roleplayingsystem.core.firebase.FirebaseTable.GAME_CLASSES;

public class GameClassesUsecase extends BaseGameTEditInteractorImpl<GameClassModel>
    implements GameClassesInteractor {

  public GameClassesUsecase() {
    super(GameClassModel.class);
  }

  @Override public DatabaseReference getDatabaseReference(GameModel gameModel) {
    return FireBaseUtils.getTableReference(GAME_CLASSES).child(gameModel.getId());
  }
}
      