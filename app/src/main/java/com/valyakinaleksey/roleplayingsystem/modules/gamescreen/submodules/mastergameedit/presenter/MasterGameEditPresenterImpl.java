package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.presenter.BasePresenter;
import com.valyakinaleksey.roleplayingsystem.core.utils.RxTransformers;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.InfoSection;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.StaticItem;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.TwoValueExpandableSectionImpl;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.EditableSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.SimpleSingleValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.adapter.viewmodel.TwoValueEditModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.EditGameInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameCharacteristicsUseCase;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.interactor.GameClassesInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacteristicModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.adapter.MasterGameSection;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.MasterGameEditView;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.mastergameedit.view.model.MasterGameEditModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import timber.log.Timber;

import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_MASTER_GAME_DESCRIPTION;
import static com.valyakinaleksey.roleplayingsystem.utils.AdapterConstants.TYPE_MASTER_GAME_NAME;

public class MasterGameEditPresenterImpl
    extends BasePresenter<MasterGameEditView, MasterGameEditModel>
    implements MasterGameEditPresenter {

  private EditGameInteractor editGameInteractor;
  private GameCharacteristicsInteractor gameCharacteristicsInteractor;
  private GameClassesInteractor gameClassesInteractor;

  public MasterGameEditPresenterImpl(EditGameInteractor editGameInteractor,
      GameCharacteristicsInteractor gameCharacteristicsInteractor,
      GameClassesInteractor gameClassesInteractor) {
    this.editGameInteractor = editGameInteractor;
    this.gameCharacteristicsInteractor = gameCharacteristicsInteractor;
    this.gameClassesInteractor = gameClassesInteractor;
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
        Observable.zip(gameCharacteristicsInteractor.getCharacteristicsByGameModel(model),
            gameClassesInteractor.getClassesByGameModel(model),
            (gameCharacteristicModels, gameClassModels) -> (getInfoSections(
                gameCharacteristicModels, gameClassModels)))
            .compose(RxTransformers.applySchedulers())
            .compose(RxTransformers.applyOpBeforeAndAfter(showLoading, hideLoading))
            .subscribe(infoSections -> {
              viewModel.setInfoSections(infoSections);
              view.setData(viewModel);
              view.showContent();
            }, Crashlytics::logException));
  }

  @NonNull @SuppressWarnings("unchecked") private ArrayList<InfoSection> getInfoSections(
      List<GameCharacteristicModel> gameCharacteristicModels, List<GameClassModel> gameClasses) {
    ArrayList<InfoSection> infoSections = new ArrayList<>();
    ArrayList<StaticItem> data = new ArrayList<>();
    data.add(new StaticItem(TYPE_MASTER_GAME_NAME, getTitleModel()));
    data.add(new StaticItem(TYPE_MASTER_GAME_DESCRIPTION, getDescriptionModel()));
    MasterGameSection section = new MasterGameSection(data);
    infoSections.add(section);
    addCharacteristicsSection(infoSections, gameCharacteristicModels);
    addClassesSection(infoSections, gameClasses);
    return infoSections;
  }

  @NonNull private EditableSingleValueEditModel getTitleModel() {
    EditableSingleValueEditModel singleValueEditModel = new EditableSingleValueEditModel();
    singleValueEditModel.setTitle(RpsApp.app().getString(R.string.name));
    singleValueEditModel.setValue(viewModel.getGameModel().getName());
    singleValueEditModel.setValueHint(RpsApp.app().getString(R.string.super_game));
    singleValueEditModel.setSaveOnclickListener(s -> {
      GameModel gameModel = viewModel.getGameModel();
      if (!gameModel.getName().equals(s)) {
        gameModel.setName(s);
        compositeSubscription.add(
            editGameInteractor.saveField(gameModel, GameModel.FIELD_NAME, gameModel.getName())
                .subscribe(s1 -> {
                  Timber.d("Success save name " + s1);
                }, Crashlytics::logException));
      }
    });
    return singleValueEditModel;
  }

  @NonNull private EditableSingleValueEditModel getDescriptionModel() {
    EditableSingleValueEditModel singleValueEditModel = new EditableSingleValueEditModel();
    singleValueEditModel.setTitle(RpsApp.app().getString(R.string.description));
    singleValueEditModel.setValue(viewModel.getGameModel().getDescription());
    singleValueEditModel.setValueHint(RpsApp.app().getString(R.string.here_could_be_description));
    singleValueEditModel.setSaveOnclickListener(s -> {
      GameModel gameModel = viewModel.getGameModel();
      if (!gameModel.getDescription().equals(s)) {
        gameModel.setDescription(s);
        compositeSubscription.add(
            editGameInteractor.saveField(gameModel, GameModel.FIELD_DESCRIPTION,
                gameModel.getDescription()).subscribe(s1 -> {
              Timber.d("Success save name " + s1);
            }, Crashlytics::logException));
      }
    });
    return singleValueEditModel;
  }

  private void addCharacteristicsSection(ArrayList<InfoSection> infoSections,
      List<GameCharacteristicModel> gameCharacteristicModel) {
    ArrayList<TwoValueEditModel> data = new ArrayList<>();
    for (GameCharacteristicModel characteristicModel : gameCharacteristicModel) {
      data.add(getTwoValueEditCharacteristicModel(characteristicModel));
    }
    TwoValueExpandableSectionImpl section =
        new TwoValueExpandableSectionImpl(StringUtils.getStringById(R.string.characteristics),
            StringUtils.getStringById(R.string.add_characteristic), data,
            () -> getTwoValueEditCharacteristicModel(new GameCharacteristicModel()));
    infoSections.add(section);
  }

  @NonNull private TwoValueEditModel getTwoValueEditCharacteristicModel(
      GameCharacteristicModel characteristicModel) {
    TwoValueEditModel twoValueEditModel = new TwoValueEditModel();
    twoValueEditModel.setId(characteristicModel.getId());
    twoValueEditModel.setMainValue(new SimpleSingleValueEditModel(characteristicModel.getName(),
        StringUtils.getStringById(R.string.name), s -> {
      characteristicModel.setName(s);
      characteristicModel.setDescription(twoValueEditModel.getSecondaryValue().getValue());
      if (TextUtils.isEmpty(twoValueEditModel.getId())) {
        gameCharacteristicsInteractor.createGameCharacteristic(viewModel.getGameModel(),
            characteristicModel).compose(RxTransformers.applySchedulers()).subscribe(s1 -> {
          characteristicModel.setId(s1);
          twoValueEditModel.setId(s1);
        }, Crashlytics::logException);
      } else {
        gameCharacteristicsInteractor.editGameCharacteristic(viewModel.getGameModel(),
            characteristicModel, FireBaseUtils.FIELD_NAME, characteristicModel.getName())
            .compose(RxTransformers.applySchedulers())
            .subscribe(gameCharacteristicModel -> {

            }, Crashlytics::logException);
      }
    }));
    twoValueEditModel.setSecondaryValue(
        new SimpleSingleValueEditModel(characteristicModel.getDescription(),
            StringUtils.getStringById(R.string.description), s -> {
          characteristicModel.setDescription(s);
          gameCharacteristicsInteractor.editGameCharacteristic(viewModel.getGameModel(),
              characteristicModel, FireBaseUtils.FIELD_DESCRIPTION,
              characteristicModel.getDescription())
              .compose(RxTransformers.applySchedulers())
              .subscribe(gameCharacteristicModel -> {

              }, Crashlytics::logException);
        }));
    twoValueEditModel.setDeleteOnClickListener(twoValueEditModel1 -> {
      gameCharacteristicsInteractor.deleteCharacteristic(viewModel.getGameModel(),
          characteristicModel).compose(RxTransformers.applySchedulers()).subscribe(aBoolean -> {

      }, Crashlytics::logException);
    });
    return twoValueEditModel;
  }

  private void addClassesSection(ArrayList<InfoSection> infoSections,
      List<GameClassModel> gameClassModels) {
    ArrayList<TwoValueEditModel> data = new ArrayList<>();
    for (GameClassModel classModel : gameClassModels) {
      data.add(getTwoValueClassModel(classModel));
    }
    TwoValueExpandableSectionImpl section =
        new TwoValueExpandableSectionImpl(StringUtils.getStringById(R.string.classes),
            StringUtils.getStringById(R.string.add_class), data,
            () -> getTwoValueClassModel(new GameClassModel()));
    infoSections.add(section);
  }

  @NonNull private TwoValueEditModel getTwoValueClassModel(GameClassModel gameClassModel) {
    TwoValueEditModel twoValueEditModel = new TwoValueEditModel();
    twoValueEditModel.setId(gameClassModel.getId());
    twoValueEditModel.setMainValue(new SimpleSingleValueEditModel(gameClassModel.getName(),
        StringUtils.getStringById(R.string.name), s -> {
      gameClassModel.setName(s);
      gameClassModel.setDescription(twoValueEditModel.getSecondaryValue().getValue());
      if (TextUtils.isEmpty(twoValueEditModel.getId())) {
        gameClassesInteractor.createGameClass(viewModel.getGameModel(), gameClassModel)
            .compose(RxTransformers.applySchedulers())
            .subscribe(s1 -> {
              gameClassModel.setId(s1);
              twoValueEditModel.setId(s1);
            }, Crashlytics::logException);
      } else {
        gameClassesInteractor.editGameClass(viewModel.getGameModel(), gameClassModel,
            FireBaseUtils.FIELD_NAME, gameClassModel.getName())
            .compose(RxTransformers.applySchedulers())
            .subscribe(gameCharacteristicModel -> {

            }, Crashlytics::logException);
      }
    }));
    twoValueEditModel.setSecondaryValue(
        new SimpleSingleValueEditModel(gameClassModel.getDescription(),
            StringUtils.getStringById(R.string.description), s -> {
          gameClassModel.setDescription(s);
          gameClassesInteractor.editGameClass(viewModel.getGameModel(), gameClassModel,
              FireBaseUtils.FIELD_DESCRIPTION, gameClassModel.getDescription())
              .compose(RxTransformers.applySchedulers())
              .subscribe(gameCharacteristicModel -> {

              }, Crashlytics::logException);
        }));
    twoValueEditModel.setDeleteOnClickListener(
        twoValueEditModel1 -> gameClassesInteractor.deleteClass(viewModel.getGameModel(),
            gameClassModel).compose(RxTransformers.applySchedulers()).subscribe(aBoolean -> {

        }, Crashlytics::logException));
    return twoValueEditModel;
  }
}
