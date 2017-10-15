package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.os.Parcel;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.ImageUtils;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

public class GameCharacterListItemWithUser extends
    AbstractGameCharacterListItem<GameCharacterListItemWithUser.GameCharacterListItemWithUserViewHolder> {

  private User user;

  @Override public GameCharacterListItemWithUserViewHolder createViewHolder(FlexibleAdapter adapter,
      LayoutInflater inflater, ViewGroup parent) {
    return new GameCharacterListItemWithUserViewHolder(
        inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter,
      GameCharacterListItemWithUserViewHolder holder, int position, List payloads) {
    holder.bind(this, getGamesCharactersPresenter());
  }

  public static class GameCharacterListItemWithUserViewHolder
      extends AbstractGameCharacterItemViewHolder {

    @BindView(R.id.user_avatar) AppCompatImageView userAvatar;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_description) TextView userDescription;

    public GameCharacterListItemWithUserViewHolder(View view, FlexibleAdapter adapter) {
      super(view, adapter);
    }

    public void bind(GameCharacterListItemWithUser gameCharacterListItemWithUser,
        GamesCharactersPresenter gamesCharactersPresenter) {
      super.bind(gameCharacterListItemWithUser, gamesCharactersPresenter);
      User user = gameCharacterListItemWithUser.getUser();
      userName.setText(user.getName());
      userDescription.setText(getUserDescription(user));
      ImageUtils.loadAvatar(userAvatar, user);
    }

    private CharSequence getUserDescription(User user) {
      return StringUtils.getStringById(R.string.participated_in) + " " + StringUtils.getPluralById(
          R.plurals.games_count_in, user.getCountOfGamesPlayed() + user.getCountOfGamesMastered());
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  public GameCharacterListItemWithUser() {
  }

  protected GameCharacterListItemWithUser(Parcel in) {
    super(in);
  }

  public static final Creator<GameCharacterListItemWithUser> CREATOR =
      new Creator<GameCharacterListItemWithUser>() {
        @Override public GameCharacterListItemWithUser createFromParcel(Parcel source) {
          return new GameCharacterListItemWithUser(source);
        }

        @Override public GameCharacterListItemWithUser[] newArray(int size) {
          return new GameCharacterListItemWithUser[size];
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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
    return R.layout.game_character_item_occupied;
  }
}
      