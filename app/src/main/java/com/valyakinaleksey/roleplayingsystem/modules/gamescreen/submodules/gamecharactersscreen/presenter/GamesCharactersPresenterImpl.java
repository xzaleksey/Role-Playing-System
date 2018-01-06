package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter;

import android.content.Context;
import android.os.Bundle;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.rx.DataObserver;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.customview.AnimatedTitlesLayout;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.CharactersFilterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.domain.GameCharactersInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.GamesCharactersView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.AbstractGameCharacterListItem;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel;
import com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.UUID;

import static com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model.GamesCharactersViewModel.*;
import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.getStringById;

public class GamesCharactersPresenterImpl
        extends BasePresenter<GamesCharactersView, GamesCharactersViewModel>
        implements GamesCharactersPresenter {

    private GameCharactersInteractor gameCharactersInteractor;
    private BehaviorSubject<CharactersFilterModel> subject = BehaviorSubject.create();

    public GamesCharactersPresenterImpl(GameCharactersInteractor gameCharactersInteractor) {
        this.gameCharactersInteractor = gameCharactersInteractor;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GamesCharactersViewModel initNewViewModel(Bundle arguments) {
        final GamesCharactersViewModel gamesCharactersViewModel = new GamesCharactersViewModel();
        GameModel gameModel = arguments.getParcelable(GameModel.KEY);
        gamesCharactersViewModel.setGameModel(gameModel);
        gamesCharactersViewModel.setMaster(
                gameModel.getMasterId().equals(FireBaseUtils.getCurrentUserId()));
        initTitleNav(gamesCharactersViewModel);
        return gamesCharactersViewModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        super.getData();
        subject.onNext(new CharactersFilterModel(viewModel.getNavigationTab()));
        compositeSubscription.add(
                gameCharactersInteractor.observeCharacters(viewModel.getGameModel(), subject)
                        .compose(RxTransformers.applySchedulers())
                        .subscribe(model -> {
                            for (IFlexible<?> item : model.getIFlexibles()) {
                                if (item instanceof AbstractGameCharacterListItem) {
                                    ((AbstractGameCharacterListItem) item).setGamesCharactersPresenter(this);
                                }
                            }
                            viewModel.setGameCharactersItemsModel(model);
                            view.showContent();
                        }, this::handleThrowable));
    }

    @Override
    public void createCharacter() {
        GameCharacterModel model = new GameCharacterModel();
        model.setName("My Character");
        if (UUID.randomUUID().hashCode() % 2 == 0) {
            model.setUid(FireBaseUtils.getCurrentUserId());
        }
        gameCharactersInteractor.createGameTModel(viewModel.getGameModel(), model).subscribe(s -> {

        }, this::handleThrowable);
    }

    @Override
    public void restoreViewModel(GamesCharactersViewModel viewModel) {
        super.restoreViewModel(viewModel);
        initTitleNav(viewModel);
    }

    @Override
    public void play(Context context, AbstractGameCharacterListItem abstractGameCharacterListItem) {
        if (!viewModel.isUpdatedRequired() && context != null) {
            gameCharactersInteractor.chooseCharacter(viewModel.getGameModel(),
                    abstractGameCharacterListItem.getGameCharacterModel()).subscribe(aVoid -> {

            }, this::handleThrowable);
        }
    }

    @Override
    public void changeNpcVisibility(GameCharacterModel gameCharacterModel, boolean isVisible) {
        compositeSubscription.add(
                gameCharactersInteractor.changeCharacterVisibility(viewModel.getGameModel(),
                        gameCharacterModel)
                        .compose(RxTransformers.applyIoSchedulers())
                        .subscribe(new DataObserver<Void>() {
                            @Override
                            public void onData(Void data) {
                                Timber.d("success change Npc visibility");
                            }
                        }));
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
            subject.onNext(new CharactersFilterModel(index));
        });
        return titleModel;
    }
}
