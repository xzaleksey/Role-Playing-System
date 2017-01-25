package com.valyakinaleksey.roleplayingsystem.modules.gamedescription.presenter;

import android.os.Bundle;

import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.MaterialDrawableProviderImpl;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.PerFragmentScope;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.DefaultExpandableSectionImpl;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticFieldsSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.TwoLineTextWithAvatarExpandableSectionImpl;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.AvatarWithTwoLineTextModel;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.model.TwoOrThreeLineTextModel;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.ObserveUsersInGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.domain.interactor.JoinGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.GamesDescriptionView;
import com.valyakinaleksey.roleplayingsystem.modules.gamedescription.view.model.GamesDescriptionModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.parentscreen.presenter.ParentPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants;
import com.valyakinaleksey.roleplayingsystem.utils.NavigationUtils;

import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.ArrayList;

import java.util.List;
import rx.Observable;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.ELEMENT_TYPE_USERS_EXPANDABLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TITLE;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_TWO_LINE_WITH_AVATAR;
import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.formatWithCount;
import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.getStringById;

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
    viewModel.setEmpty(true);
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    GameModel gameModel = viewModel.getGameModel();

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
              view.showContent();
              observeGameInfo(gameModel);
              observeUsers();
            }, Crashlytics::logException));
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
              User userModel = userInGameModel.getUser();
              for (InfoSection infoSection : viewModel.getInfoSections()) {
                if (infoSection.getSectionType() == ELEMENT_TYPE_USERS_EXPANDABLE) {
                  ArrayList<AvatarWithTwoLineTextModel> data = infoSection.getData();
                  switch (userInGameModel.getEventType()) {
                    case ADDED:
                      addUser(data, userModel);
                      break;
                    case REMOVED:
                      for (AvatarWithTwoLineTextModel avatarWithTwoLineTextModel : data) {
                        if (avatarWithTwoLineTextModel.getModel().equals(userModel)) {
                          data.remove(avatarWithTwoLineTextModel);
                          break;
                        }
                      }
                      break;
                  }
                  break;
                }
              }
              view.updateView();
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
          bundle.putBoolean(NavigationUtils.POP_BACKSTACK, true);
          parentPresenter.navigateToFragment(NavigationUtils.GAME_FRAGMENT, bundle);
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
        new AvatarWithTwoLineTextModel(gameModel.getMasterName(), "Провел много игр",
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
    avatarWithTwoLineTextModels.add(
        new AvatarWithTwoLineTextModel(userModel.getName(), "Провел много игр",
            new MaterialDrawableProviderImpl(userModel.getName(), userModel.getUid()),
            userModel.getPhotoUrl(), userModel));
  }
}
