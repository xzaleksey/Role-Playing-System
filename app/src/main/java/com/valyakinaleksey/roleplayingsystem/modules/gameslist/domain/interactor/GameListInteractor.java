package com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.interactor;

import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.List;
import rx.Observable;

public interface GameListInteractor {
  Observable<List<IFlexible<?>>> observeGameViewModels();
}
