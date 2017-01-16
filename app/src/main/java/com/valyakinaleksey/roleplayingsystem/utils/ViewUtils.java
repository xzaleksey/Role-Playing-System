package com.valyakinaleksey.roleplayingsystem.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

public class ViewUtils {
    public static int DEFUALT_INCREASE_VALUE = (int) RpsApp.app().getResources().getDimension(R.dimen.common_margin_between_elements);

    public static void increaseTouchArea(View delegate) {
        increaseTouchArea(delegate, DEFUALT_INCREASE_VALUE);
    }

    public static void increaseTouchArea(View delegate, int extraPadding) {
        final View parent = (View) delegate.getParent();
        parent.post(() -> {
            final Rect r = new Rect();
            delegate.getHitRect(r);
            r.top -= extraPadding;
            r.left -= extraPadding;
            r.right += extraPadding;
            r.bottom += extraPadding;
            parent.setTouchDelegate(new TouchDelegate(r, delegate));
        });
    }
}
      