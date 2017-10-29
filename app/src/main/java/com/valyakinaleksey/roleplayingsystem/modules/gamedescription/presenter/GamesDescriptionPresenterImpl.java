package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.MaterialDrawableProviderImpl;
import com.valyakinaleksey.roleplayingsystem.core.model.DataEvent;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.*;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.AvatarWithTwoLineTextModel;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.characterisitics.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants;
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationScreen;
import com.valyakinaleksey.roleplayingsystem.utils.navigation.NavigationUtils;
import rx.Observable;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.*;
import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.*;

@PerFragmentScope public class GamesDescriptionPresenterImpl
    extends BasePresenter<GamesDescriptionView, GamesDescriptionModel>
    implements GamesDescriptionPresenter {

  private UserGetInteractor userGetInteractor;
  private JoinGameInteractor joinGameInteractor;
  private ObserveGameInteractor observeGameInteractor;
  private ObserveUsersInGameInteractor observeUsersInGameInteractor;
  private ParentPresenter parentPresenter;
  private GameCharacteristicsInteractor gameCharacteristicsInteractor;
  private GameClassesInteractor gameClassesInteractor;
  private GameRacesInteractor gameRacesInteractor;

  public GamesDescriptionPresenterImpl(UserGetInteractor userGetInteractor,
      JoinGameInteractor joinGameInteractor, ObserveGameInteractor observeGameInteractor,
      ObserveUsersInGameInteractor observeUsersInGameInteractor, ParentPresenter parentPresenter,
      GameCharacteristicsInteractor gameCharacteristicsInteractor,
      GameClassesInteractor gameClassesInteractor, GameRacesInteractor gameRacesInteractor) {
    this.userGetInteractor = userGetInteractor;
    this.joinGameInteractor = joinGameInteractor;
    this.observeGameInteractor = observeGameInteractor;
    this.observeUsersInGameInteractor = observeUsersInGameInteractor;
    this.parentPresenter = parentPresenter;
    this.gameCharacteristicsInteractor = gameCharacteristicsInteractor;
    this.gameClassesInteractor = gameClassesInteractor;
    this.gameRacesInteractor = gameRacesInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected GamesDescriptionModel initNewViewModel(Bundle arguments) {
    final GamesDescriptionModel gamesDescriptionModel = new GamesDescriptionModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    gamesDescriptionModel.setToolbarTitle(gameModel.getName());
    gamesDescriptionModel.setGameModel(gameModel);

    return gamesDescriptionModel;
  }

  @Override public void restoreViewModel(GamesDescriptionModel viewModel) {
    super.restoreViewModel(viewModel);
    Timber.d("restoreModel");
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    GameModel gameModel = viewModel.getGameModel();
    Timber.d("Game description getData");
    view.preFillModel(viewModel);
    compositeSubscription.add(
        Observable.zip(userGetInteractor.getUserByUid(gameModel.getMasterId()),
            gameCharacteristicsInteractor.getValuesByGameModel(gameModel),
            gameClassesInteractor.getValuesByGameModel(gameModel),
            gameRacesInteractor.getValuesByGameModel(gameModel),
            (user, gameCharacteristicModels, gameClassModels, gameRaceModels) -> {
              updateInfoSections(gameModel, user, gameCharacteristicModels, gameClassModels,
                  gameRaceModels);
              return viewModel.getInfoSections();
            })
            .compose(RxTransformers.applySchedulers())
            .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
            .subscribe(user -> {
              view.setData(viewModel);
              viewModel.setNeedUpdate(false);
              view.showContent();
              observeGameInfo(gameModel);
              observeUsers();
            }, this::handleThrowable));
  }

  private void observeGameInfo(GameModel gameModel) {
    compositeSubscription.add(
        observeGameInteractor.observeGameModelChanged(viewModel.getGameModel())
            .subscribe(gameModel1 -> {
              ArrayList<StaticItem> data =
                  ((StaticFieldsSection) viewModel.getInfoSections().get(0)).getData();
              data.get(0).setValue(gameModel.getDescription());
              viewModel.setToolbarTitle(gameModel.getName());
              view.updateView();
            }, Crashlytics::logException));
  }

  private void observeUsers() {
    compositeSubscription.add(
        observeUsersInGameInteractor.observeUserJoinedToGame(viewModel.getGameModel().getId())
            .subscribe(userInGameModel -> {
              InfoSection userInfosection = null;
              User userModel = userInGameModel.getUser();
              for (InfoSection infoSection : viewModel.getInfoSections()) {
                if (infoSection.getSectionType() == ELEMENT_TYPE_USERS_EXPANDABLE) {
                  userInfosection = infoSection;
                  ArrayList<AvatarWithTwoLineTextModel> data = infoSection.getData();
                  switch (userInGameModel.getEventType()) {
                    case ADDED:
                      addUser(data, userModel);
                      infoSection.setTitle(
                          formatWithCount(getStringById(R.string.game_players), data.size()));
                      view.updateView(userInfosection,
                          new DataEvent(DataEvent.EventType.ADDED, data.size()));
                      break;
                    case REMOVED:
                      int index = -1;
                      AvatarWithTwoLineTextModel avatarWithTwoLineTextModel = null;
                      for (int i = 0; i < data.size(); i++) {
                        avatarWithTwoLineTextModel = data.get(i);
                        if (avatarWithTwoLineTextModel.getModel().equals(userModel)) {
                          index = i;
                          break;
                        }
                      }
                      if (index >= 0) {
                        data.remove(avatarWithTwoLineTextModel);
                        infoSection.setTitle(
                            formatWithCount(getStringById(R.string.game_players), data.size()));
                        view.updateView(userInfosection,
                            new DataEvent(DataEvent.EventType.REMOVED, index));
                      }
                      break;
                  }
                  break;
                }
              }
            }, Crashlytics::logException));
  }

  @Override public UserGetInteractor getValue() {
    return userGetInteractor;
  }

  @Override public void joinGame() {
    compositeSubscription.add(joinGameInteractor.joinGame(viewModel.getGameModel())
        .compose(RxTransformers.applySchedulers())
        .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
        .subscribe(aBoolean -> {
          GameModel gameModel = viewModel.getGameModel();
          Bundle bundle = new Bundle();
          bundle.putParcelable(GameModel.KEY, gameModel);
          bundle.putBoolean(NavigationUtils.POP_BACK_STACK, true);
          bundle.putBoolean(NavigationUtils.ADD_BACK_STACK, true);
          parentPresenter.navigateToFragment(NavigationScreen.GAME_FRAGMENT, bundle);
        }, throwable -> {
          Timber.d(throwable);
          Crashlytics.logException(throwable);
        }));
  }

  @SuppressWarnings("unchecked") private void updateInfoSections(GameModel gameModel, User user,
      List<GameCharacteristicModel> gameCharacteristicModels, List<GameClassModel> gameClassModels,
      List<GameRaceModel> gameRaceModels) {
    ArrayList<InfoSection> infoSections = new ArrayList<>();
    ArrayList<StaticItem> data = new ArrayList<>();
    data.add(new StaticItem(TYPE_DESCRIPTION, gameModel.getDescription()));
    data.add(new StaticItem(TYPE_TITLE, RpsApp.app().getString(R.string.master_of_the_game)));
    data.add(new StaticItem(TYPE_TWO_LINE_WITH_AVATAR,
        new AvatarWithTwoLineTextModel(gameModel.getMasterName(),
            getStringById(R.string.led).concat(" ") + getPluralById(R.plurals.games_count_mastered,
                user.getCountOfGamesMastered()),
            new MaterialDrawableProviderImpl(gameModel.getMasterName(), gameModel.getMasterId()),
            user.getPhotoUrl(), gameModel.getMasterId())));
    infoSections.add(new StaticFieldsSection(data));

    addSection(
        formatWithCount(getStringById(R.string.characteristics), gameCharacteristicModels.size()),
        gameCharacteristicModels, infoSections);

    addSection(formatWithCount(getStringById(R.string.classes), gameClassModels.size()),
        gameClassModels, infoSections);

    addSection(formatWithCount(getStringById(R.string.races), gameRaceModels.size()),
        gameRaceModels, infoSections);
    ArrayList<AvatarWithTwoLineTextModel> avatarWithTwoLineTextModels = new ArrayList<>();
    infoSections.add(new TwoLineTextWithAvatarExpandableSectionImpl(
        AdapterConstants.ELEMENT_TYPE_USERS_EXPANDABLE,
        formatWithCount(getStringById(R.string.game_players), avatarWithTwoLineTextModels.size()),
        avatarWithTwoLineTextModels));
    viewModel.setInfoSections(infoSections);
  }

  private <T extends HasName & HasDescription> void addSection(String title,
      List<T> gameCharacteristicModels, ArrayList<InfoSection> infoSections) {
    ArrayList<TwoOrThreeLineTextModel> twoOrThreeLineTextModels = new ArrayList<>();
    for (T model : gameCharacteristicModels) {
      String description = model.getDescription();
      twoOrThreeLineTextModels.add(new TwoOrThreeLineTextModel(model.getName(),
          TextUtils.isEmpty(description) ? getStringById(R.string.here_could_be_description)
              : description));
    }
    infoSections.add(new DefaultExpandableSectionImpl(title, twoOrThreeLineTextModels));
  }

  private void addUser(ArrayList<AvatarWithTwoLineTextModel> avatarWithTwoLineTextModels,
      User userModel) {
    avatarWithTwoLineTextModels.add(new AvatarWithTwoLineTextModel(userModel.getName(),
        getStringById(R.string.took_part).concat(" ") + getPluralById(R.plurals.games_count_in,
            userModel.getCountOfGamesPlayed()),
        new MaterialDrawableProviderImpl(userModel.getName(), userModel.getUid()),
        userModel.getPhotoUrl(), userModel));
  }
}
