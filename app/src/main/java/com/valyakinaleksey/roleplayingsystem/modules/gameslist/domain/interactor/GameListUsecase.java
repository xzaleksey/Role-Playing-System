package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel;
import com.valyakinaleksey.roleplayingsystem.data.repository.game.GameRepository;
import com.valyakinaleksey.roleplayingsystem.data.repository.user.UserRepository;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.model.GameListItemViewModel;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.functions.Func2;

public class GameListUsecase implements GameListInteractor {

  private UserRepository userRepository;
  private GameRepository gameRepository;

  public GameListUsecase(UserRepository userRepository, GameRepository gameRepository) {
    this.userRepository = userRepository;
    this.gameRepository = gameRepository;
  }

  @Override public Observable<List<IFlexible<?>>> observeGameViewModels() {
    return gameRepository.observeGames()
        .concatMap(stringGameModelMap -> userRepository.getUsersMap().map(stringUserMap -> {
          List<IFlexible<?>> gameListItemViewModels = new ArrayList<>();
          for (GameModel gameModel : stringGameModelMap.values()) {
            gameListItemViewModels.add(
                new GameListItemViewModel(gameModel, getPhotoUrl(stringUserMap, gameModel)));
          }
          return gameListItemViewModels;
        }))
        .onBackpressureLatest();
  }

  @Override public Observable<List<IFlexible<?>>> observeGameViewModelsWithFilter(
      Observable<FilterModel> filterModelObservable) {
    return Observable.combineLatest(
        gameRepository.observeGames().throttleLast(100, TimeUnit.MILLISECONDS),
        filterModelObservable,
        new Func2<Map<String, GameModel>, FilterModel, Observable<List<IFlexible<?>>>>() {
          @Override
          public Observable<List<IFlexible<?>>> call(Map<String, GameModel> stringGameModelMap,
              FilterModel filterModel) {
            return userRepository.getUsersMap().map(stringUserMap -> {
              List<IFlexible<?>> gameListItemViewModels = new ArrayList<>();
              String query = filterModel.getQuery().toLowerCase();
              for (GameModel gameModel : stringGameModelMap.values()) {
                if (StringUtils.isEmpty(query) || gameModel.getName()
                    .toLowerCase()
                    .startsWith(query)) {
                  gameListItemViewModels.add(
                      new GameListItemViewModel(gameModel, getPhotoUrl(stringUserMap, gameModel)));
                }
              }
              return gameListItemViewModels;
            });
          }
        }).concatMap(listObservable -> listObservable).onBackpressureLatest();
  }

  private String getPhotoUrl(Map<String, User> stringUserMap, GameModel gameModel) {
    User user = stringUserMap.get(gameModel.getMasterId());
    return user == null ? StringUtils.EMPTY_STRING : user.getPhotoUrl();
  }
}
