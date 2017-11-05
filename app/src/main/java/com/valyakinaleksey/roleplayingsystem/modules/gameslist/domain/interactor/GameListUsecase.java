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
import rx.Observable;

public class GameListUsecase implements GameListInteractor {

  public static final int INTERVAL_DURATION = 100;
  private UserRepository userRepository;
  private GameRepository gameRepository;

  public GameListUsecase(UserRepository userRepository, GameRepository gameRepository) {
    this.userRepository = userRepository;
    this.gameRepository = gameRepository;
  }

  @Override public Observable<List<IFlexible<?>>> observeGameViewModels() {
    return gameRepository.observeData()
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

  @Override public Observable<GameListResult> observeGameViewModelsWithFilter(
      Observable<FilterModel> filterModelObservable) {
    return Observable.combineLatest(
        gameRepository.observeData(),
        filterModelObservable, (stringGameModelMap, filterModel) -> userRepository.getUsersMap().map(stringUserMap -> {
          List<IFlexible<?>> gameListItemViewModels = new ArrayList<>();
          String query = filterModel.getQuery().toLowerCase();
          for (GameModel gameModel : stringGameModelMap.values()) {
            if (StringUtils.isEmpty(query) || gameModel.getName()
                .toLowerCase()
                .startsWith(query) || gameModel.getMasterName()
                .toLowerCase()
                .startsWith(query)) {
              gameListItemViewModels.add(
                  new GameListItemViewModel(gameModel, getPhotoUrl(stringUserMap, gameModel)));
            }
          }
          return new GameListResult(gameListItemViewModels, filterModel);
        })).concatMap(listObservable -> listObservable).onBackpressureLatest();
  }

  private String getPhotoUrl(Map<String, User> stringUserMap, GameModel gameModel) {
    User user = stringUserMap.get(gameModel.getMasterId());
    return user == null ? StringUtils.EMPTY_STRING : user.getPhotoUrl();
  }
}
