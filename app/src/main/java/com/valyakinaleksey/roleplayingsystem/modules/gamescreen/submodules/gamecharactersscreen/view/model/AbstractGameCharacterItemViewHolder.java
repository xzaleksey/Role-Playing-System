package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.view.model;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameClassModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameRaceModel;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.gamecharactersscreen.presenter.GamesCharactersPresenter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class AbstractGameCharacterItemViewHolder extends FlexibleViewHolder {
  @Bind(R.id.avatar) AppCompatImageView avatar;
  @Bind(R.id.character_name) TextView characterName;
  @Bind(R.id.character_class_race) TextView characterClassRace;

  public AbstractGameCharacterItemViewHolder(View view, FlexibleAdapter adapter) {
    super(view, adapter);
    ButterKnife.bind(this, view);
  }

  public void bind(AbstractGameCharacterListItem abstractGameCharacterListItem,
      GamesCharactersPresenter gamesCharactersPresenter) {
    avatar.setImageResource(R.drawable.ic_done_black_24dp);
    characterName.setText(abstractGameCharacterListItem.getGameCharacterModel().getName());
    GameRaceModel gameRaceModel = abstractGameCharacterListItem.getGameRaceModel();
    GameClassModel gameClassModelModel = abstractGameCharacterListItem.getGameClassModel();
    characterClassRace.setText(
        gameRaceModel.getName().concat(", ").concat(gameClassModelModel.getName()));

}
}
      