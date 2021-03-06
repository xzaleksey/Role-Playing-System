package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor;

import com.google.firebase.database.DataSnapshot;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.GameRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;

import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel.FIELD_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel.FIELD_NAME;

public class ObserveGameUseCase implements ObserveGameInteractor {

    private GameRepository gameRepository;

    public ObserveGameUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Observable<GameModel> observeGameModel(GameModel gameModel) {
        return gameRepository.observeGameById(gameModel.getId())
                .filter(firebaseChildEvent -> {
                    DataSnapshot dataSnapshot = firebaseChildEvent.getDataSnapshot();
                    String key = dataSnapshot.getKey();
                    if (key.equals(FIELD_DESCRIPTION)) {
                        gameModel.setDescription(dataSnapshot.getValue(String.class));
                        return true;
                    } else if (key.equals(FIELD_NAME)) {
                        gameModel.setName(dataSnapshot.getValue(String.class));
                        return true;
                    }
                    return false;
                }).map(firebaseChildEvent -> gameModel);
    }
}
      