package com.valyakinaleksey.roleplayingsystem.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

public class ImageUtils {

    public static void setTintVectorImage(ImageView view, int resId, int color) {
        Context context = view.getContext();
        Drawable d = VectorDrawableCompat.create(context.getResources(), resId,
                null);
        d = DrawableCompat.wrap(d);
        DrawableCompat.setTint(d, color);
        view.setImageDrawable(d);
    }
}
