package com.valyakinaleksey.roleplayingsystem.core.view.adapter.viewholder;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import rx.Subscription;

public class GameListViewHolder extends FlexibleViewHolder {
  @BindView(R.id.avatar) ImageView ivAvatar;
  @BindView(R.id.master_name) TextView tvMasterName;
  @BindView(R.id.name) TextView tvName;
  @BindView(R.id.date) TextView tvDate;
  @BindView(R.id.iv_password) ImageView ivPassword;

  protected Subscription subscription;

  public GameListViewHolder(View itemView, FlexibleAdapter flexibleAdapter) {
    super(itemView, flexibleAdapter);
    ButterKnife.bind(this, itemView);
  }

  public void bind(GameModel model, String photoUrl) {
    setName(model.getName());
    setMasterName(model.getMasterName());
    setDate(model.getDateCreateLong());
    updateLock(TextUtils.isEmpty(model.getPassword()));
    updateAvatar(model.getMasterId(), model.getMasterName(), photoUrl);
  }


  private void setMasterName(String masterName) {
    tvMasterName.setText(masterName);
  }

  public void setName(String name) {
    tvName.setText(name);
  }

  private void updateLock(boolean empty) {
    ivPassword.setVisibility(empty ? View.GONE : View.VISIBLE);
  }

  private void setDate(long dateCreate) {
    tvDate.setText(new DateTime(dateCreate).toString(
        DateTimeFormat.mediumDate().withLocale(Locale.getDefault())));
  }

  private void updateAvatar(String uid, String masterName, String photoUrl) {
    if (subscription != null) {
      subscription.unsubscribe();
    }
    TextDrawable textDrawable = getTextDrawable(uid, masterName);
    ivAvatar.setImageDrawable(textDrawable);
    if (!StringUtils.isEmpty(photoUrl)) {
      Glide.with(ivAvatar.getContext())
          .load(photoUrl)
          .asBitmap()
          .centerCrop()
          .placeholder(textDrawable)
          .error(textDrawable)
          .into(new BitmapImageViewTarget(ivAvatar) {
            @Override protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(ivAvatar.getContext().getResources(),
                      resource);
              circularBitmapDrawable.setCircular(true);
              ivAvatar.setImageDrawable(circularBitmapDrawable);
            }
          });
    }
  }

  private TextDrawable getTextDrawable(String uid, String masterName) {
    ColorGenerator generator = ColorGenerator.MATERIAL;
    return TextDrawable.builder()
        .beginConfig()
        .useFont(RpsApp.getFont())
        .toUpperCase()
        .endConfig()
        .buildRound(masterName.substring(0, 1), generator.getColor(uid));
  }
}
      