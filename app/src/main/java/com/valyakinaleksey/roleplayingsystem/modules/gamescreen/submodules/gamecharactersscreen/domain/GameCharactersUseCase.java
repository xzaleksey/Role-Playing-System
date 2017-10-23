package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain;

import android.text.TextUtils;
import com.google.firebase.database.DatabaseReference;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.firebase.AccessFirebaseException;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.abstractions.BaseGameTEditInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GameCharacterListItemNPC;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GameCharacterListItemWithUser;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GameCharacterListItemWithoutUser;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.List;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERS;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.getTableReference;

public class GameCharactersUseCase extends BaseGameTEditInteractorImpl<GameCharacterModel>
    implements GameCharactersInteractor {

  private GameClassesInteractor gameClassesInteractor;
  private GameRacesInteractor gameRacesInteractor;
  private UserRepository userRepository;
  private CharactersRepository charactersRepository;

  public GameCharactersUseCase(GameClassesInteractor gameClassesInteractor,
      GameRacesInteractor gameRacesInteractor, UserRepository userRepository,
      CharactersRepository charactersRepository) {
    super(GameCharacterModel.class);
    this.gameClassesInteractor = gameClassesInteractor;
    this.gameRacesInteractor = gameRacesInteractor;
    this.userRepository = userRepository;
    this.charactersRepository = charactersRepository;
  }

  @Override public DatabaseReference getDatabaseReference(GameModel gameModel) {
    return getTableReference(GAME_CHARACTERS).child(gameModel.getId());
  }

  @Override public Observable<List<IFlexible<?>>> observeCharacters(GameModel gameModel,
      Observable<CharactersFilterModel> charactersFilterModelObservable) {

    //Observable.combineLatest(charactersRepository.observeCharacters(),
    //    charactersFilterModelObservable, (stringGameCharacterModelMap, charactersFilterModel) -> {
    //      for (GameCharacterModel gameCharacterModel : stringGameCharacterModelMap.values()) {
    //
    //      }
    //    })
        return null;
  }

  @Override public Observable<AbstractGameCharacterListItem> getAbstractGameCharacterListItem(
      GameModel gameModel, GameCharacterModel gameCharacterModel) {
    AbstractGameCharacterListItem abstractGameCharacterListItem;
    boolean free = TextUtils.isEmpty(gameCharacterModel.getUid());
    if (free) {
      abstractGameCharacterListItem = new GameCharacterListItemWithoutUser();
    } else if (gameCharacterModel.getUid().equals(gameModel.getMasterId())) {
      abstractGameCharacterListItem = new GameCharacterListItemNPC();
    } else {
      abstractGameCharacterListItem = new GameCharacterListItemWithUser();
    }
    abstractGameCharacterListItem.setGameModel(gameModel);
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
      if (TextUtils.isEmpty(classId)) {
        GameClassModel gameClassModel = new GameClassModel();
        gameClassModel.setName("");
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

  @Override public Observable<Void> chooseCharacter(GameModel gameModel,
      GameCharacterModel gameCharacterModel) {
    return getSingleChild(gameModel, gameCharacterModel.getId()).switchMap(gameCharacterModel1 -> {
      if (TextUtils.isEmpty(gameCharacterModel1.getUid())) {
        return FireBaseUtils.setData(FireBaseUtils.getCurrentUserId(),
            getDatabaseReference(gameModel).child(gameCharacterModel.getId())
                .child(FireBaseUtils.UID));
      } else {
        return Observable.error(new AccessFirebaseException());
      }
    });
  }

  @Override public Observable<Void> changeCharacterVisibility(GameModel gameModel,
      GameCharacterModel gameCharacterModel) {
    return FireBaseUtils.setData(true,
        getDatabaseReference(gameModel).child(gameCharacterModel.getId())
            .child(GameCharacterModel.VISIBLE));
  }
}
      