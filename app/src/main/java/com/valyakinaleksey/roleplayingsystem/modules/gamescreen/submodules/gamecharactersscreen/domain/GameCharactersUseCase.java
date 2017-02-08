package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain;

import android.text.TextUtils;
import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.BaseGameTEditInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GameCharacterListItemWithUser;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GameCharacterListItemWithoutUser;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.getTableReference;

public class GameCharactersUseCase extends BaseGameTEditInteractorImpl<GameCharacterModel>
    implements GameCharactersInteractor {

  private GameClassesInteractor gameClassesInteractor;
  private GameRacesInteractor gameRacesInteractor;
  private UserRepository userRepository;

  public GameCharactersUseCase(GameClassesInteractor gameClassesInteractor,
      GameRacesInteractor gameRacesInteractor, UserRepository userRepository) {
    super(GameCharacterModel.class);
    this.gameClassesInteractor = gameClassesInteractor;
    this.gameRacesInteractor = gameRacesInteractor;
    this.userRepository = userRepository;
  }

  @Override public DatabaseReference getDatabaseReference(GameModel gameModel) {
    return getTableReference(GAME_CHARACTERS).child(gameModel.getId());
  }

  @Override public Observable<AbstractGameCharacterListItem> getAbstractGameCharacterListItem(
      GameModel gameModel, GameCharacterModel gameCharacterModel) {
    AbstractGameCharacterListItem abstractGameCharacterListItem;
    boolean free = TextUtils.isEmpty(gameCharacterModel.getUid());
    if (free) {
      abstractGameCharacterListItem = new GameCharacterListItemWithoutUser();
    } else {
      abstractGameCharacterListItem = new GameCharacterListItemWithUser();
    }
    abstractGameCharacterListItem.setGameCharacterModel(gameCharacterModel);
    Observable<AbstractGameCharacterListItem> abstractGameCharacterListItemObservable;
    if (free) {
      abstractGameCharacterListItemObservable = Observable.just(abstractGameCharacterListItem);
    } else {
      abstractGameCharacterListItemObservable = Observable.just(abstractGameCharacterListItem)
          .zipWith(userRepository.getUserByUid(gameCharacterModel.getUid()),
              (abstractGameCharacterListItem1, user) -> {
                ((GameCharacterListItemWithUser) abstractGameCharacterListItem1).setUser(user);
                return abstractGameCharacterListItem;
              });
    }

    return abstractGameCharacterListItemObservable.switchMap(abstractGameCharacterListItem1 -> {
      String classId = gameCharacterModel.getClassId();
      if (TextUtils.isEmpty(classId)) { //TODO remove in future
        GameClassModel gameClassModel = new GameClassModel();
        gameClassModel.setName(StringUtils.getStringById(R.string.warrior));
        abstractGameCharacterListItem.setGameClassModel(gameClassModel);
        return Observable.just(abstractGameCharacterListItem1);
      } else {
        return gameClassesInteractor.getSingleChild(gameModel, classId).map(gameClassModel -> {
          abstractGameCharacterListItem1.setGameClassModel(gameClassModel);
          return abstractGameCharacterListItem1;
        });
      }
    }).switchMap(abstractGameCharacterListItem1 -> {
      String raceId = gameCharacterModel.getRaceId();
      if (TextUtils.isEmpty(raceId)) {
        GameRaceModel gameRaceModel = new GameRaceModel();
        gameRaceModel.setName(StringUtils.getStringById(R.string.human));
        abstractGameCharacterListItem.setGameRaceModel(gameRaceModel);
        return Observable.just(abstractGameCharacterListItem1);
      } else {
        return gameRacesInteractor.getSingleChild(gameModel, raceId).map(gameRaceModel -> {
          abstractGameCharacterListItem1.setGameRaceModel(gameRaceModel);
          return abstractGameCharacterListItem1;
        });
      }
    });
  }
}
      