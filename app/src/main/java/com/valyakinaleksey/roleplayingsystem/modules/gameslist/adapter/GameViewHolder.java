package com.valyakinaleksey.roleplayingsystem.modules.gameslist.adapter;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.crashlytics.android.Crashlytics;
import com.ezhome.rxfirebase2.auth.RxFirebaseAuth;
import com.ezhome.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.core.view.adapter.ButterKnifeViewHolder;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.interactor.UserGetInteractor;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.modules.gameslist.domain.model.GameModel;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import rx.Subscription;

public class GameViewHolder extends ButterKnifeViewHolder {
    @Bind(R.id.avatar)
    protected ImageView ivAvatar;
    @Bind(R.id.master_name)
    protected TextView tvMasterName;
    @Bind(R.id.name)
    protected TextView tvName;
    @Bind(R.id.date)
    protected TextView tvDate;
    @Bind(R.id.iv_password)
    protected ImageView ivPassword;

    protected Subscription subscription;

    public GameViewHolder(View itemView) {
        super(itemView);
    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void bind(GameModel model, UserGetInteractor userGetInteractor) {
        setName(model.getName());
        setMasterName(model.getMasterName());
        setDate(model.getDateCreate());
        updateLock(TextUtils.isEmpty(model.getPassword()));
        updateAvatar(model.getMasterId(), model.getMasterName(), userGetInteractor);
    }

    private void updateLock(boolean empty) {
        ivPassword.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void setDate(long dateCreate) {
        tvDate.setText(new DateTime(dateCreate).toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault())));
    }

    public void setMasterName(String masterName) {
        tvMasterName.setText(masterName);
    }

    public void updateAvatar(String uid, String masterName, UserGetInteractor userGetInteractor) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable textDrawable = TextDrawable.builder().beginConfig()
                .useFont(RpsApp.getFont())
                .toUpperCase()
                .endConfig()
                .buildRound(masterName.substring(0, 1), generator.getColor(uid));
        ivAvatar.setImageDrawable(textDrawable);
        subscription = userGetInteractor.getUserByUid(uid)
                .subscribe(user -> {
                    Glide.with(ivAvatar.getContext()).load(user.getPhotoUrl())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(textDrawable)
                            .error(textDrawable)
                            .into(new BitmapImageViewTarget(ivAvatar) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(ivAvatar.getContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivAvatar.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }, Crashlytics::logException);
    }
}
      