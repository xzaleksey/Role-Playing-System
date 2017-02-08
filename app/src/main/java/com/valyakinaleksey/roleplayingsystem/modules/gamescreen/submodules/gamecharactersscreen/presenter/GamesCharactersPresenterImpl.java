package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter;

import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.flexible.SubHeaderViewModel;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import rx.Observable;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.GAME_CHARACTERS;

public class GamesCharactersPresenterImpl
    extends BasePresenter<GamesCharactersView, GamesCharactersViewModel>
    implements GamesCharactersPresenter {

  private GameCharactersInteractor gameCharactersInteractor;
  private List<IFlexible> itemsAll = new ArrayList<>();
  private SubHeaderViewModel free;

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
    return gamesCharactersViewModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    viewModel.setiFlexibles(new ArrayList<>(itemsAll));
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
          if (itemsAll.isEmpty()) {
            SubHeaderViewModel occupied = new SubHeaderViewModel();
            free = new SubHeaderViewModel();
            free.setTitle(StringUtils.getStringById(R.string.free));
            itemsAll.add(occupied);
            itemsAll.add(free);
            occupied.setTitle(StringUtils.getStringById(R.string.occupied));
          }
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
                  }
                  break;
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
          List<IFlexible> iFlexibles = new ArrayList<>(itemsAll);
          if (!viewModel.isMaster()) {
            boolean hasChar = false;
            for (IFlexible iFlexible : iFlexibles) {
              if (iFlexible instanceof AbstractGameCharacterListItem) {
                if (FireBaseUtils.getCurrentUserId()
                    .equals(((AbstractGameCharacterListItem) iFlexible).getGameCharacterModel()
                        .getUid())) {
                  hasChar = true;
                  break;
                }
              }
            }
            viewModel.setHasCharacter(hasChar);
            if (hasChar) {
              iFlexibles = iFlexibles.subList(0, iFlexibles.indexOf(free));
            }
          }
          view.setData(viewModel);
          viewModel.setiFlexibles(iFlexibles);
          view.showContent();
        }, this::handleThrowable));
  }

  @NonNull private Observable<?> getAddObservable(GameModel gameModel,
      RxFirebaseChildEvent<GameCharacterModel> gameCharacterModelRxFirebaseChildEvent) {
    return getCharacterFilled(gameModel, gameCharacterModelRxFirebaseChildEvent).doOnNext(
        abstractGameCharacterListItem -> {
          abstractGameCharacterListItem.setGamesCharactersPresenter(this);
          abstractGameCharacterListItem.setMaster(
              viewModel.getGameModel().getMasterId().equals(FireBaseUtils.getCurrentUserId()));
          if (TextUtils.isEmpty(abstractGameCharacterListItem.getGameCharacterModel().getUid())) {
            itemsAll.add(abstractGameCharacterListItem);
          } else {
            itemsAll.add(itemsAll.indexOf(free), abstractGameCharacterListItem);
          }
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

  @Override
  public void play(Context context, AbstractGameCharacterListItem abstractGameCharacterListItem) {
    if (!viewModel.isEmpty()) {
      gameCharactersInteractor.chooseCharacter(viewModel.getGameModel(),
          abstractGameCharacterListItem.getGameCharacterModel()).subscribe(aVoid -> {

      }, this::handleThrowable);
    }
  }
}
