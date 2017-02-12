package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class GameCharacterListItemWithoutUser extends
    AbstractGameCharacterListItem<GameCharacterListItemWithoutUser.GameCharacterListItemWithoutUserViewHolder> {

  @Override
  public GameCharacterListItemWithoutUserViewHolder createViewHolder(FlexibleAdapter adapter,
      LayoutInflater inflater, ViewGroup parent) {
    return new GameCharacterListItemWithoutUserViewHolder(
        inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      GameCharacterListItemWithoutUserViewHolder holder, int position, List payloads) {
    holder.bind(this, getGamesCharactersPresenter());
  }

  public static class GameCharacterListItemWithoutUserViewHolder
      extends AbstractGameCharacterItemViewHolder {

    @Bind(R.id.btn_play) Button play;
    @Bind(R.id.character_description) TextView tvCharacterDescription;

    public GameCharacterListItemWithoutUserViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
    }

    public void bind(AbstractGameCharacterListItem abstractGameCharacterListItem,
        GamesCharactersPresenter gamesCharactersPresenter) {

      super.bind(abstractGameCharacterListItem, gamesCharactersPresenter);
      play.setOnClickListener(
          v -> gamesCharactersPresenter.play(itemView.getContext(), abstractGameCharacterListItem));
      play.setText(abstractGameCharacterListItem.getGameModel()
          .getMasterId()
          .equals(FireBaseUtils.getCurrentUserId()) ? StringUtils.getStringById(R.string.make_npc)
          : StringUtils.getStringById(R.string.play));
      tvCharacterDescription.setText(
          abstractGameCharacterListItem.getGameCharacterModel().getDescription());
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  public GameCharacterListItemWithoutUser() {
  }

  protected GameCharacterListItemWithoutUser(Parcel in) {
    super(in);
  }

  public static final Creator<GameCharacterListItemWithoutUser> CREATOR =
      new Creator<GameCharacterListItemWithoutUser>() {
        @Override public GameCharacterListItemWithoutUser createFromParcel(Parcel source) {
          return new GameCharacterListItemWithoutUser(source);
        }

        @Override public GameCharacterListItemWithoutUser[] newArray(int size) {
          return new GameCharacterListItemWithoutUser[size];
        }
      };

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
    return R.layout.game_character_item_free;
  }
}
      