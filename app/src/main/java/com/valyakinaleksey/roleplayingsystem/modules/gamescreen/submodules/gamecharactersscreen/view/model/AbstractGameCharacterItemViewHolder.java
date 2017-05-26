package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
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
    avatar.setImageResource(R.drawable.ic_done_black_24dp);
    characterName.setText(abstractGameCharacterListItem.getGameCharacterModel().getName());
    GameRaceModel gameRaceModel = abstractGameCharacterListItem.getGameRaceModel();
    GameClassModel gameClassModel = abstractGameCharacterListItem.getGameClassModel();

    String name = gameRaceModel.getName();
    if (!TextUtils.isEmpty(gameClassModel.getName())) {
      name = name.concat(", ").concat(gameClassModel.getName());
    }
    characterClassRace.setText(name);
  }
}
      