package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import com.valyakinaleksey.roleplayingsystem.core.model.FilterModel;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.List;
import rx.Observable;

public interface GameListInteractor {
  Observable<List<IFlexible<?>>> observeGameViewModels();
  Observable<GameListResult> observeGameViewModelsWithFilter(Observable<FilterModel> filterModelObservable);
}
