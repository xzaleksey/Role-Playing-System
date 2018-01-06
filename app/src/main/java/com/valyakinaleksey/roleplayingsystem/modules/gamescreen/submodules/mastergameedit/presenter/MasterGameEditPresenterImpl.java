package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasDescription;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasId;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.HasName;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.TwoValueExpandableSectionImpl;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.EditableSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SimpleSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.TwoValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.game.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.characterisitics.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.classes.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.races.GameRacesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.abstractions.GameTEditInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.adapter.MasterGameSection;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;
import com.valyakinaleksey.roleplayingsystem.utils.ModelMapper;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_MASTER_GAME_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_MASTER_GAME_NAME;
import static com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils.FIELD_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.core.firebase.FireBaseUtils.FIELD_NAME;

public class MasterGameEditPresenterImpl
    extends BasePresenter<MasterGameEditView, MasterGameEditModel>
    implements MasterGameEditPresenter {

  private EditGameInteractor editGameInteractor;
  private GameCharacteristicsInteractor gameCharacteristicsInteractor;
  private GameClassesInteractor gameClassesInteractor;
  private GameRacesInteractor gameRacesInteractorInteractor;

  public MasterGameEditPresenterImpl(EditGameInteractor editGameInteractor,
      GameCharacteristicsInteractor gameCharacteristicsInteractor,
      GameClassesInteractor gameClassesInteractor,
      GameRacesInteractor gameRacesInteractorInteractor) {
    this.editGameInteractor = editGameInteractor;
    this.gameCharacteristicsInteractor = gameCharacteristicsInteractor;
    this.gameClassesInteractor = gameClassesInteractor;
    this.gameRacesInteractorInteractor = gameRacesInteractorInteractor;
  }

  @SuppressWarnings("unchecked") @Override
  protected MasterGameEditModel initNewViewModel(Bundle arguments) {
    final MasterGameEditModel masterGameEditModel = new MasterGameEditModel();
    GameModel gameModel = arguments.getParcelable(GameModel.KEY);
    masterGameEditModel.setGameModel(gameModel);
    return masterGameEditModel;
  }

  @SuppressWarnings("unchecked") @Override public void getData() {
    GameModel model = viewModel.getGameModel();
    compositeSubscription.add(
        Observable.zip(gameCharacteristicsInteractor.getValuesByGameModel(model),
            gameClassesInteractor.getValuesByGameModel(model),
            gameRacesInteractorInteractor.getValuesByGameModel(model),
            (gameCharacteristicModels, gameClassModels, gameRaceModels) -> (getInfoSections(
                gameCharacteristicModels, gameClassModels, gameRaceModels)))
            .compose(RxTransformers.applySchedulers())
            .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
            .subscribe(infoSections -> {
              viewModel.setInfoSections(infoSections);
              viewModel.setNeedUpdate(false);
              view.setData(viewModel);
              view.showContent();
            }, this::handleThrowable));
  }

  @NonNull @SuppressWarnings("unchecked") private ArrayList<InfoSection> getInfoSections(
      List<GameCharacteristicModel> gameCharacteristicModels, List<GameClassModel> gameClasses,
      List<GameRaceModel> gameRaceModels) {
    ArrayList<InfoSection> infoSections = new ArrayList<>();
    ArrayList<StaticItem> data = new ArrayList<>();
    data.add(new StaticItem(TYPE_MASTER_GAME_NAME, getTitleModel()));
    data.add(new StaticItem(TYPE_MASTER_GAME_DESCRIPTION, getDescriptionModel()));
    MasterGameSection section = new MasterGameSection(data);
    infoSections.add(section);
    addCharacteristicsSection(infoSections, gameCharacteristicModels);
    addClassesSection(infoSections, gameClasses);
    addRacesSection(infoSections, gameRaceModels);
    return infoSections;
  }

  @NonNull private EditableSingleValueEditModel getTitleModel() {
    return ModelMapper.getEditableSingleValueEditModel(RpsApp.app().getString(R.string.name),
        viewModel.getGameModel().getName(), RpsApp.app().getString(R.string.super_game), s -> {
          GameModel gameModel = viewModel.getGameModel();
          if (!gameModel.getName().equals(s)) {
            gameModel.setName(s);
            compositeSubscription.add(
                editGameInteractor.saveField(gameModel, FIELD_NAME, gameModel.getName())
                    .subscribe(s1 -> {
                      Timber.d("Success save name " + s1);
                    }, Crashlytics::logException));
          }
        });
  }

  private void addCharacteristicsSection(ArrayList<InfoSection> infoSections,
      List<GameCharacteristicModel> gameCharacteristicModel) {
    ArrayList<TwoValueEditModel> data = new ArrayList<>();
    for (GameCharacteristicModel characteristicModel : gameCharacteristicModel) {
      data.add(getTwoValueEditModel(gameCharacteristicsInteractor, characteristicModel));
    }
    TwoValueExpandableSectionImpl section =
        new TwoValueExpandableSectionImpl(StringUtils.getStringById(R.string.characteristics),
            StringUtils.getStringById(R.string.add_characteristic), data,
            () -> getTwoValueEditModel(gameCharacteristicsInteractor,
                new GameCharacteristicModel()));
    infoSections.add(section);
  }

  private void addClassesSection(ArrayList<InfoSection> infoSections,
      List<GameClassModel> gameClassModels) {
    ArrayList<TwoValueEditModel> data = new ArrayList<>();
    for (GameClassModel classModel : gameClassModels) {
      data.add(getTwoValueEditModel(gameClassesInteractor, classModel));
    }
    TwoValueExpandableSectionImpl section =
        new TwoValueExpandableSectionImpl(StringUtils.getStringById(R.string.classes),
            StringUtils.getStringById(R.string.add_class), data,
            () -> getTwoValueEditModel(gameClassesInteractor, new GameClassModel()));
    infoSections.add(section);
  }

  private void addRacesSection(ArrayList<InfoSection> infoSections,
      List<GameRaceModel> gameRaceModels) {
    ArrayList<TwoValueEditModel> data = new ArrayList<>();
    for (GameRaceModel raceModel : gameRaceModels) {
      data.add(getTwoValueEditModel(gameRacesInteractorInteractor, raceModel));
    }
    TwoValueExpandableSectionImpl section =
        new TwoValueExpandableSectionImpl(StringUtils.getStringById(R.string.races),
            StringUtils.getStringById(R.string.add_race), data,
            () -> getTwoValueEditModel(gameRacesInteractorInteractor, new GameRaceModel()));
    infoSections.add(section);
  }

  @NonNull private EditableSingleValueEditModel getDescriptionModel() {
    return ModelMapper.getEditableSingleValueEditModel(RpsApp.app().getString(R.string.description),
        viewModel.getGameModel().getDescription(),
        RpsApp.app().getString(R.string.here_could_be_description), s -> {
          GameModel gameModel = viewModel.getGameModel();
          if (!gameModel.getDescription().equals(s)) {
            gameModel.setDescription(s);
            compositeSubscription.add(editGameInteractor.saveField(gameModel, FIELD_DESCRIPTION,
                gameModel.getDescription()).subscribe(s1 -> {
              Timber.d("Success save name " + s1);
            }, Crashlytics::logException));
          }
        });
  }

  private <T extends HasId & HasName & HasDescription> TwoValueEditModel getTwoValueEditModel(
      GameTEditInteractor<T> tGameTEditInteractor, T model) {
    TwoValueEditModel twoValueEditModel = new TwoValueEditModel();
    twoValueEditModel.setId(model.getId());
    twoValueEditModel.setMainValue(
        new SimpleSingleValueEditModel(model.getName(), StringUtils.getStringById(R.string.name),
            s -> {
              model.setName(s);
              model.setDescription(twoValueEditModel.getSecondaryValue().getValue());
              if (TextUtils.isEmpty(twoValueEditModel.getId())) {
                tGameTEditInteractor.createGameTModel(viewModel.getGameModel(), model)
                    .compose(RxTransformers.applySchedulers())
                    .subscribe(s1 -> {
                      model.setId(s1);
                      twoValueEditModel.setId(s1);
                    }, Crashlytics::logException);
              } else {
                tGameTEditInteractor.editGameTmodel(viewModel.getGameModel(), model, FIELD_NAME,
                    model.getName())
                    .compose(RxTransformers.applySchedulers())
                    .subscribe(gameCharacteristicModel -> {

                    }, Crashlytics::logException);
              }
            }));
    twoValueEditModel.setSecondaryValue(new SimpleSingleValueEditModel(model.getDescription(),
        StringUtils.getStringById(R.string.description), s -> {
      model.setDescription(s);
      tGameTEditInteractor.editGameTmodel(viewModel.getGameModel(), model, FIELD_DESCRIPTION,
          model.getDescription())
          .compose(RxTransformers.applySchedulers())
          .subscribe(gameCharacteristicModel -> {

          }, Crashlytics::logException);
    }));
    twoValueEditModel.setOnItemClickListener(
        twoValueEditModel1 -> tGameTEditInteractor.deleteTModel(viewModel.getGameModel(), model)
            .compose(RxTransformers.applySchedulers())
            .subscribe(aBoolean -> {

            }, Crashlytics::logException));
    return twoValueEditModel;
  }
}
