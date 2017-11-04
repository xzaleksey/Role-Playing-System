package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Transaction;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.data.CharactersRepository;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.abstractions.BaseGameTEditInteractorImpl;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.IdDateModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.*;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.*;

public class GameCharactersUseCase extends BaseGameTEditInteractorImpl<GameCharacterModel>
        implements GameCharactersInteractor {

    private CharactersRepository charactersRepository;

    public GameCharactersUseCase(CharactersRepository charactersRepository) {
        super(GameCharacterModel.class);
        this.charactersRepository = charactersRepository;
    }

    @Override
    public DatabaseReference getDatabaseReference(GameModel gameModel) {
        return getTableReference(GAME_CHARACTERS).child(gameModel.getId());
    }

    @Override
    public Observable<GameCharactersItemsModel> observeCharacters(GameModel gameModel,
                                                                  Observable<CharactersFilterModel> charactersFilterModelObservable) {
        String currentUserId = getCurrentUserId();
        String masterId = gameModel.getMasterId();
        final boolean isMaster = masterId.equals(currentUserId);
        return Observable.combineLatest(getMappedModels(gameModel), charactersFilterModelObservable,
                (models, charactersFilterModel) -> {
                    boolean hasCharacter = isMaster;
                    List<IFlexible<?>> filteredModels = new ArrayList<>();
                    for (AbstractGameCharacterListItem model : models) {
                        switch (charactersFilterModel.getType()) {
                            case GamesCharactersViewModel.FREE_TAB:
                                if (model.getType() == GamesCharactersViewModel.FREE_TAB) {
                                    filteredModels.add(model);
                                }
                                break;
                            case GamesCharactersViewModel.OCCUPIED_TAB:
                                if (model.getType() == GamesCharactersViewModel.OCCUPIED_TAB) {
                                    filteredModels.add(model);
                                }
                                break;

                            case GamesCharactersViewModel.NPC_TAB:
                                if (model.getType() == GamesCharactersViewModel.NPC_TAB) {
                                    filteredModels.add(model);
                                }
                                break;
                        }
                        if (currentUserId.equals(model.getGameCharacterModel().getUid())) {
                            hasCharacter = true;
                        }
                    }
                    return new GameCharactersItemsModel(filteredModels, hasCharacter);
                });
    }

    private Observable<List<AbstractGameCharacterListItem>> getMappedModels(GameModel gameModel) {
        String currentUserId = getCurrentUserId();
        String masterId = gameModel.getMasterId();
        boolean isMaster = masterId.equals(currentUserId);
        return charactersRepository.observeData()
                .throttleLast(50, TimeUnit.MILLISECONDS)
                .map(stringGameCharacterModelMap -> {
                    ArrayList<AbstractGameCharacterListItem> abstractGameCharacterListItems =
                            new ArrayList<>();
                    ArrayList<GameCharacterListItemWithoutUser> gameCharacterListItemWithoutUsers =
                            new ArrayList<>();
                    boolean hasCharacter = isMaster;
                    for (GameCharacterModel gameCharacterModel : stringGameCharacterModelMap.values()) {
                        AbstractGameCharacterListItem abstractGameCharacterListItem;
                        if (gameCharacterModel.user == null) {
                            GameCharacterListItemWithoutUser gameCharacterListItemWithoutUser =
                                    new GameCharacterListItemWithoutUser();
                            abstractGameCharacterListItem = gameCharacterListItemWithoutUser;
                            gameCharacterListItemWithoutUsers.add(gameCharacterListItemWithoutUser);
                        } else if (masterId.equals(gameCharacterModel.getUid())) {
                            abstractGameCharacterListItem = new GameCharacterListItemNPC();
                        } else {
                            if (currentUserId.equals(gameCharacterModel.getUid())) {
                                hasCharacter = true;
                            }
                            abstractGameCharacterListItem = new GameCharacterListItemWithUser();
                        }
                        if (isMaster || gameCharacterModel.visible) {
                            abstractGameCharacterListItem.setGameCharacterModel(gameCharacterModel);
                            abstractGameCharacterListItem.setGameModel(gameModel);
                            abstractGameCharacterListItem.setMaster(isMaster);
                            abstractGameCharacterListItems.add(abstractGameCharacterListItem);
                        }
                    }
                    if (!hasCharacter) {
                        for (GameCharacterListItemWithoutUser gameCharacterListItemWithoutUser : gameCharacterListItemWithoutUsers) {
                            gameCharacterListItemWithoutUser.setShowPlayButton(true);
                        }
                    }
                    return abstractGameCharacterListItems;
                });
    }

    @Override
    public Observable<Void> chooseCharacter(GameModel gameModel,
                                            GameCharacterModel gameCharacterModel) {
        return FireBaseUtils.startTransaction(getDatabaseReference(gameModel).child(gameCharacterModel.getId()), data -> {
                    GameCharacterModel characterModel = data.getValue(GameCharacterModel.class);
                    if (characterModel == null || !StringUtils.isEmpty(characterModel.getUid())) {
                        return Transaction.success(data);
                    }
                    String currentUserId = FireBaseUtils.getCurrentUserId();
                    characterModel.setId(currentUserId);
                    data.setValue(characterModel);
                    FireBaseUtils.getTableReference(CHARACTERS_IN_USER)
                            .child(currentUserId)
                            .child(gameModel.getId()).setValue(
                            new IdDateModel(gameCharacterModel.getId(), System.currentTimeMillis()));
                    return Transaction.success(data);
                }
        );
    }

    @Override
    public Observable<Void> changeCharacterVisibility(GameModel gameModel,
                                                      GameCharacterModel gameCharacterModel) {
        return FireBaseUtils.setData(true,
                getDatabaseReference(gameModel).child(gameCharacterModel.getId())
                        .child(GameCharacterModel.VISIBLE));
    }
}
      