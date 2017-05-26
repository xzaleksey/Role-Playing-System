package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

public class GameCharacterListItemWithoutUser extends
    AbstractGameCharacterListItem<GameCharacterListItemWithoutUser.GameCharacterListItemWithoutUserViewHolder> {
  private boolean showPlayButton;

  public boolean isShowPlayButton() {
    return showPlayButton;
  }

  public void setShowPlayButton(boolean showPlayButton) {
    this.showPlayButton = showPlayButton;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GameCharacterListItemWithoutUser)) return false;
    if (!super.equals(o)) return false;

    GameCharacterListItemWithoutUser that = (GameCharacterListItemWithoutUser) o;

    return isShowPlayButton() == that.isShowPlayButton();
  }

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

    @BindView(R.id.btn_play) Button play;
    @BindView(R.id.character_description) TextView tvCharacterDescription;

    public GameCharacterListItemWithoutUserViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
    }

    public void bind(GameCharacterListItemWithoutUser gameCharacterListItemWithoutUser,
        GamesCharactersPresenter gamesCharactersPresenter) {

      super.bind(gameCharacterListItemWithoutUser, gamesCharactersPresenter);
      if (gameCharacterListItemWithoutUser.isShowPlayButton()) {
        tvCharacterDescription.setMaxLines(2);
        play.setVisibility(View.VISIBLE);
        play.setOnClickListener(v -> gamesCharactersPresenter.play(itemView.getContext(),
            gameCharacterListItemWithoutUser));
        play.setText(gameCharacterListItemWithoutUser.getGameModel()
            .getMasterId()
            .equals(FireBaseUtils.getCurrentUserId()) ? StringUtils.getStringById(R.string.make_npc)
            : StringUtils.getStringById(R.string.play));
        tvCharacterDescription.setText(
            gameCharacterListItemWithoutUser.getGameCharacterModel().getDescription());
      } else {
        play.setVisibility(View.GONE);
        tvCharacterDescription.setMaxLines(3);
      }
    }
  }

  public GameCharacterListItemWithoutUser() {
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
    return R.layout.game_character_item_free;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeByte(this.showPlayButton ? (byte) 1 : (byte) 0);
  }

  protected GameCharacterListItemWithoutUser(Parcel in) {
    super(in);
    this.showPlayButton = in.readByte() != 0;
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
}
      