package com.valyakinaleksey.roleplayingsystem.modules.mygames.adapter;

import android.view.View;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.mygames.presenter.MyGamesListPresenter;
import rx.Subscription;

public class MyGameViewHolder extends ButterKnifeViewHolder {

  protected Subscription subscription;

  public MyGameViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(GameModel model, MyGamesListPresenter myGamesListPresenter) {

  }
}
      