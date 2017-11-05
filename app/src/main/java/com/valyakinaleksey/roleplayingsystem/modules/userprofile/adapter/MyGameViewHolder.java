package com.valyakinaleksey.roleplayingsystem.modules.userprofile.adapter;

import android.view.View;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.modules.userprofile.presenter.UserProfilePresenter;
import rx.Subscription;

public class MyGameViewHolder extends ButterKnifeViewHolder {

  protected Subscription subscription;

  public MyGameViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(GameModel model, UserProfilePresenter userProfilePresenter) {

  }
}
      