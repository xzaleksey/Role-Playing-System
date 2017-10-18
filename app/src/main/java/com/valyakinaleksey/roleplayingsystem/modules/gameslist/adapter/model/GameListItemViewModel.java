package com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter.model;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.GameListViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.io.Serializable;
import java.util.List;

public class GameListItemViewModel extends AbstractFlexibleItem<GameListViewHolder>
    implements Serializable {

  private GameModel gameModel;
  private String photoUrl;

  public GameListItemViewModel(GameModel gameModel, String photoUrl) {
    this.gameModel = gameModel;
    this.photoUrl = photoUrl;
  }

  public GameModel getGameModel() {
    return gameModel;
  }

  @Override
  public GameListViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new GameListViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, GameListViewHolder holder, int position,
      List payloads) {
    holder.bind(gameModel, photoUrl);
  }

  @Override public int getLayoutRes() {
    return R.layout.games_list_item;
  }

  @Override public boolean equals(Object o) {
    return this == o
        || (o instanceof GameListItemViewModel
        && ((GameListItemViewModel) o).gameModel.getId()
        .equals(gameModel.getId())
        && ((GameListItemViewModel) o).photoUrl.equals(photoUrl)
        && ((GameListItemViewModel) o).gameModel.getName().equals(gameModel.getName()))
        && ((GameListItemViewModel) o).gameModel.getMasterName().equals(gameModel.getMasterName())
        && StringUtils.areEqual(((GameListItemViewModel) o).gameModel.getPassword(),
        (gameModel.getMasterName()));
  }

  @Override public int hashCode() {
    return gameModel != null ? gameModel.getId().hashCode() : 0;
  }
}
      