package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter;

import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.customview.AnimatedTitlesLayout;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel.FREE_TAB;
import static com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel.NPC_TAB;
import static com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel.OCCUPIED_TAB;
import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERS;
import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.getStringById;

public class GamesCharactersPresenterImpl
    extends BasePresenter<GamesCharactersView, GamesCharactersViewModel>
    implements GamesCharactersPresenter {

  private GameCharactersInteractor gameCharactersInteractor;
  private List<IFlexible> itemsAll = new ArrayList<>();

  public GamesCharactersPresenterImpl(GameCharactersInteractor gameCharactersInteractor) {
    this.gameCharactersInteractor = gameCharactersInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected GamesCharactersViewModel initNewViewModel(Bundle arguments) {
    final GamesCharactersViewModel gamesCharactersViewModel = new GamesCharactersViewModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    gamesCharactersViewModel.setGameModel(gameModel);
    gamesCharactersViewModel.setMaster(
        gameModel.getMasterId().equals(FireBaseUtils.getCurrentUserId()));
    initTitleNav(gamesCharactersViewModel);
    return gamesCharactersViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    viewModel.setiFlexibles(new ArrayList<>(itemsAll));
    view.preFillModel(viewModel);
    view.showLoading();
    compositeSubscription.add(FireBaseUtils.checkReferenceExistsAndNotEmpty(
        FireBaseUtils.getTableReference(GAME_CHARACTERS).child(viewModel.getGameModel().getId()))
        .subscribe(aBoolean -> {
          if (!aBoolean) {
            view.hideLoading();
          }
        }, this::handleThrowable));
    initSubscriptions();
  }

  private void initSubscriptions() {
    GameModel gameModel = viewModel.getGameModel();
    compositeSubscription.add(gameCharactersInteractor.observeChildren(gameModel)
        .concatMap(gameCharacterModelRxFirebaseChildEvent -> {
          switch (gameCharacterModelRxFirebaseChildEvent.getEventType()) {
            case ADDED:
              return getAddObservable(gameModel, gameCharacterModelRxFirebaseChildEvent);
            case REMOVED:
              for (Iterator<IFlexible> iterator = itemsAll.iterator(); iterator.hasNext(); ) {
                IFlexible flexible = iterator.next();
                if (flexible instanceof AbstractGameCharacterListItem) {
                  if ((((AbstractGameCharacterListItem) flexible).getGameCharacterModel()).equals(
                      gameCharacterModelRxFirebaseChildEvent.getValue())) {
                    return Observable.just(itemsAll.remove(flexible));
                  }
                }
              }
            case CHANGED:
              int index = -1;
              for (Iterator<IFlexible> iterator = itemsAll.iterator(); iterator.hasNext(); ) {
                IFlexible flexible = iterator.next();
                index++;
                if (flexible instanceof AbstractGameCharacterListItem) {
                  GameCharacterModel oldValue =
                      ((AbstractGameCharacterListItem) flexible).getGameCharacterModel();
                  GameCharacterModel newValue = gameCharacterModelRxFirebaseChildEvent.getValue();

                  if ((oldValue).equals(newValue)) {
                    if (TextUtils.isEmpty(oldValue.getUid()) && !TextUtils.isEmpty(
                        newValue.getUid())) { //player occupied char
                      iterator.remove();
                      return getAddObservable(gameModel, gameCharacterModelRxFirebaseChildEvent);
                    } else if (!TextUtils.isEmpty(oldValue.getUid()) && TextUtils.isEmpty(
                        newValue.getUid())) { //player cleared char
                      iterator.remove();
                      return getAddObservable(gameModel, gameCharacterModelRxFirebaseChildEvent);
                    }
                    break;
                  }
                }
              }
              final int finalIndex = index;
              return getCharacterFilled(gameModel, gameCharacterModelRxFirebaseChildEvent).doOnNext(
                  abstractGameCharacterListItem -> itemsAll.set(finalIndex,
                      abstractGameCharacterListItem));
            default:
              return Observable.just(gameCharacterModelRxFirebaseChildEvent);
          }
        })
        .compose(RxTransformers.applySchedulers())
        .subscribe(gameCharacterModelRxFirebaseChildEvent -> {
          updateView();
        }, this::handleThrowable));
  }

  private void updateView() {
    List<IFlexible> iFlexibles = getFilteredList();
    viewModel.setiFlexibles(iFlexibles);
    view.setData(viewModel);
    view.showContent();
  }

  @NonNull private List<IFlexible> getFilteredList() {
    List<IFlexible> iFlexibles = new ArrayList<>(itemsAll);
    boolean hasChar = false;
    for (Iterator<IFlexible> iterator = iFlexibles.iterator(); iterator.hasNext(); ) {
      IFlexible iFlexible = iterator.next();
      if (iFlexible instanceof AbstractGameCharacterListItem) {
        AbstractGameCharacterListItem abstractGameCharacterListItem =
            (AbstractGameCharacterListItem) iFlexible;

        String uid = abstractGameCharacterListItem.getGameCharacterModel().getUid();
        switch (viewModel.getNavigationTab()) {
          case OCCUPIED_TAB:
            if (TextUtils.isEmpty(uid) || viewModel.getGameModel().getMasterId().equals(uid)) {
              iterator.remove();
            }
            break;
          case FREE_TAB:
            if (!TextUtils.isEmpty(uid)) {
              iterator.remove();
            }
            break;
          case NPC_TAB:
            if (!viewModel.getGameModel().getMasterId().equals(uid)) {
              iterator.remove();
            }
            break;
        }
        if (FireBaseUtils.getCurrentUserId().equals(uid)) {
          hasChar = true;
        }
      }
    }
    if (viewModel.isMaster()) hasChar = false;
    viewModel.setHasCharacter(hasChar);
    return iFlexibles;
  }

  @NonNull private Observable<?> getAddObservable(GameModel gameModel,
      RxFirebaseChildEvent<GameCharacterModel> gameCharacterModelRxFirebaseChildEvent) {
    return getCharacterFilled(gameModel, gameCharacterModelRxFirebaseChildEvent).doOnNext(
        abstractGameCharacterListItem -> {
          abstractGameCharacterListItem.setGamesCharactersPresenter(this);
          abstractGameCharacterListItem.setMaster(
              viewModel.getGameModel().getMasterId().equals(FireBaseUtils.getCurrentUserId()));
          itemsAll.add(abstractGameCharacterListItem);
        });
  }

  private Observable<AbstractGameCharacterListItem> getCharacterFilled(GameModel gameModel,
      RxFirebaseChildEvent<GameCharacterModel> gameCharacterModelRxFirebaseChildEvent) {
    return gameCharactersInteractor.getAbstractGameCharacterListItem(gameModel,
        gameCharacterModelRxFirebaseChildEvent.getValue());
  }

  @Override public void createCharacter() {
    GameCharacterModel model = new GameCharacterModel();
    model.setName("My Character");
    if (UUID.randomUUID().hashCode() % 2 == 0) {
      model.setUid(FireBaseUtils.getCurrentUserId());
    }
    gameCharactersInteractor.createGameTModel(viewModel.getGameModel(), model).subscribe(s -> {

    }, this::handleThrowable);
  }

  @Override public void restoreViewModel(GamesCharactersViewModel viewModel) {
    super.restoreViewModel(viewModel);
    initTitleNav(viewModel);
    view.preFillModel(viewModel);
    getData();
  }

  @Override
  public void play(Context context, AbstractGameCharacterListItem abstractGameCharacterListItem) {
    if (!viewModel.isEmpty() && context != null) {
      gameCharactersInteractor.chooseCharacter(viewModel.getGameModel(),
          abstractGameCharacterListItem.getGameCharacterModel()).subscribe(aVoid -> {

      }, this::handleThrowable);
    }
  }

  private void initTitleNav(GamesCharactersViewModel gamesCharactersViewModel) {
    ArrayList<AnimatedTitlesLayout.TitleModel> titleModels = new ArrayList<>();

    titleModels.add(getFilledTitleModel(OCCUPIED_TAB, getStringById(R.string.occupied)));
    titleModels.add(getFilledTitleModel(FREE_TAB, getStringById(R.string.free)));
    titleModels.add(getFilledTitleModel(NPC_TAB, getStringById(R.string.npc)));
    gamesCharactersViewModel.setTitleModels(titleModels);
  }

  private AnimatedTitlesLayout.TitleModel getFilledTitleModel(int index, String title) {
    AnimatedTitlesLayout.TitleModel titleModel = new AnimatedTitlesLayout.TitleModel();
    titleModel.setTitle(title);
    titleModel.setOnClickListener(v -> {
      viewModel.setNavigationTab(index);
      updateView();
    });
    return titleModel;
  }
}
