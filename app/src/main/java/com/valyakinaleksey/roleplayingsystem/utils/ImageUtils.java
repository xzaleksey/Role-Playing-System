package com.valyakinaleksey.roleplayingsystem.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.valyakinaleksey.roleplayingsystem.core.interfaces.MaterialDrawableProviderImpl;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;

public class ImageUtils {

  public static void setTintVectorImage(ImageView view, int resId, int color) {
    Context context = view.getContext();
    Drawable d = VectorDrawableCompat.create(context.getResources(), resId, null);
    d = DrawableCompat.wrap(d);
    DrawableCompat.setTint(d, color);
    view.setImageDrawable(d);
  }

  public static void loadAvatar(ImageView ivAvatar, Uri uri, Drawable placeHolder) {
    ivAvatar.setImageDrawable(placeHolder);
    Glide.with(ivAvatar.getContext())
        .load(uri)
        .asBitmap()
        .centerCrop()
        .placeholder(placeHolder)
        .error(placeHolder)
        .into(new BitmapImageViewTarget(ivAvatar) {
          @Override protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(ivAvatar.getContext().getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            ivAvatar.setImageDrawable(circularBitmapDrawable);
          }
        });
  }

  public static void loadAvatar(ImageView ivAvatar, User user) {
    Uri uri = null;
    try {
      uri = Uri.parse(user.getPhotoUrl());
    } catch (Exception ignored) {

    }
    Drawable placeHolder =
        new MaterialDrawableProviderImpl(user.getName(), user.getUid()).getDrawable();
    loadAvatar(ivAvatar, uri, placeHolder);
  }
}
