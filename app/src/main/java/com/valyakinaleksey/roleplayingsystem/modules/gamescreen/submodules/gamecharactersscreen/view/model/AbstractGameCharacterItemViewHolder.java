package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameCharacterModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class AbstractGameCharacterItemViewHolder extends FlexibleViewHolder {
  @BindView(R.id.avatar) AppCompatImageView avatar;
  @BindView(R.id.character_name) TextView characterName;
  @BindView(R.id.character_class_race) TextView characterClassRace;

  public AbstractGameCharacterItemViewHolder(View view, FlexibleAdapter adapter) {
    super(view, adapter);
    ButterKnife.bind(this, view);
  }

  public void bind(AbstractGameCharacterListItem abstractGameCharacterListItem,
      GamesCharactersPresenter gamesCharactersPresenter) {

    avatar.setImageDrawable(null);
    GameCharacterModel gameCharacterModel = abstractGameCharacterListItem.getGameCharacterModel();
    if (StringUtils.isEmpty(gameCharacterModel.photoUrl)) {
      avatar.setImageResource(R.drawable.mage);
    } else {
      Glide.with(itemView.getContext()).load(gameCharacterModel.photoUrl).into(avatar);
    }
    characterName.setText(gameCharacterModel.getName());
    GameRaceModel gameRaceModel = gameCharacterModel.gameRaceModel;
    GameClassModel gameClassModel = gameCharacterModel.gameClassModel;

    String name = gameRaceModel.getName();
    if (!TextUtils.isEmpty(gameClassModel.getName())) {
      name = name.concat(", ").concat(gameClassModel.getName());
    }
    characterClassRace.setText(name);
  }
}
      