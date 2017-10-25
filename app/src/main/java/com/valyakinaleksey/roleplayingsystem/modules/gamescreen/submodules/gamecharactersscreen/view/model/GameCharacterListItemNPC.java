package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

public class GameCharacterListItemNPC extends
    AbstractGameCharacterListItem<GameCharacterListItemNPC.GameCharacterListItemNPCViewHolder> {

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameCharacterListItemNPC)) return false;
    if (!super.equals(o)) return false;
    if (getGameCharacterModel().visible
        != ((AbstractGameCharacterListItem) o).getGameCharacterModel().visible) {
      return false;
    }
    return true;
  }

  @Override public GameCharacterListItemNPCViewHolder createViewHolder(FlexibleAdapter adapter,
      LayoutInflater inflater, ViewGroup parent) {
    return new GameCharacterListItemNPCViewHolder(inflater.inflate(getLayoutRes(), parent, false),
        adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, GameCharacterListItemNPCViewHolder holder,
      int position, List payloads) {
    holder.bind(this, getGamesCharactersPresenter());
  }

  public GameCharacterListItemNPC() {
    type = GamesCharactersViewModel.NPC_TAB;
  }

  @Override public boolean isEnabled() {
    return true;
  }

  @Override public void setEnabled(boolean enabled) {

  }

  @Override public boolean isHidden() {
    return false;
  }

  @Override public void setHidden(boolean hidden) {

  }

  @Override public boolean isSelectable() {
    return true;
  }

  @Override public void setSelectable(boolean selectable) {

  }

  @Override public boolean isDraggable() {
    return false;
  }

  @Override public void setDraggable(boolean draggable) {

  }

  @Override public boolean isSwipeable() {
    return false;
  }

  @Override public void setSwipeable(boolean swipeable) {

  }

  @Override public int getLayoutRes() {
    return R.layout.game_character_item_npc;
  }

  public static class GameCharacterListItemNPCViewHolder
      extends AbstractGameCharacterItemViewHolder {

    @BindView(R.id.character_description) TextView tvCharacterDescription;
    @BindView(R.id.switcher) SwitchCompat switcher;
    @BindView(R.id.master_controls) View masterControls;

    public GameCharacterListItemNPCViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
    }

    public void bind(GameCharacterListItemNPC gameCharacterListItemNPC,
        GamesCharactersPresenter gamesCharactersPresenter) {

      super.bind(gameCharacterListItemNPC, gamesCharactersPresenter);
      GameCharacterModel gameCharacterModel = gameCharacterListItemNPC.getGameCharacterModel();
      if (gameCharacterListItemNPC.isMaster()) {
        masterControls.setVisibility(View.VISIBLE);
        switcher.setChecked(gameCharacterModel.visible);
        switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
          gamesCharactersPresenter.changeNpcVisibility(
              gameCharacterListItemNPC.getGameCharacterModel(), isChecked);
        });
      } else {
        masterControls.setVisibility(View.GONE);
      }
      tvCharacterDescription.setMaxLines(3);
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  protected GameCharacterListItemNPC(Parcel in) {
    super(in);
  }

  public static final Creator<GameCharacterListItemNPC> CREATOR =
      new Creator<GameCharacterListItemNPC>() {
        @Override public GameCharacterListItemNPC createFromParcel(Parcel source) {
          return new GameCharacterListItemNPC(source);
        }

        @Override public GameCharacterListItemNPC[] newArray(int size) {
          return new GameCharacterListItemNPC[size];
        }
      };
}
      