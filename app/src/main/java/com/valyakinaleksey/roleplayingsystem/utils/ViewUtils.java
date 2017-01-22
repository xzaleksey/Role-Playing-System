package com.valyakinaleksey.roleplayingsystem.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;

import android.view.ViewParent;
import android.widget.EditText;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

public class ViewUtils {
  public static int DEFUALT_INCREASE_VALUE =
      (int) RpsApp.app().getResources().getDimension(R.dimen.common_margin_between_elements);

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

  public static View.OnTouchListener getFixRecyclerPositionOnTouchListener(EditText editText,
      int position) {
    return ((v, event) -> {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          ViewParent view = editText.getParent();
          while (view != null) {
            if (view instanceof RecyclerView) {
              break;
            }
            view = view.getParent();
          }
          if (view != null) {
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.postDelayed(() -> {
              if (recyclerView != null) {
                recyclerView.scrollToPosition(position);
                editText.requestFocus();
              }
            }, 140);
            break;
          }
      }
      return false;
    });
  }
}
      