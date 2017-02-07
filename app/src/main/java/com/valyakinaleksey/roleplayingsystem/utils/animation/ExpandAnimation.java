package com.valyakinaleksey.roleplayingsystem.utils.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandAnimation extends Animation {

  private View view;
  private int initialHeight;
  private Integer paddingBottom;

  public ExpandAnimation(View view, int initialHeight) {
    this.view = view;
    this.initialHeight = initialHeight;
    setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
        view.setVisibility(View.VISIBLE);
        view.clearFocus();
      }

      @Override public void onAnimationEnd(Animation animation) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = initialHeight;
        if (paddingBottom != null) {
          view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(),
              paddingBottom);
        }
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
  }

  @SuppressWarnings("ResourceType") @Override
  protected void applyTransformation(float interpolatedTime, Transformation t) {
    super.applyTransformation(interpolatedTime, t);
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    layoutParams.height = (int) ((initialHeight - 1) * interpolatedTime) + 1;
    if (paddingBottom != null) {
      int currentBottomPadding = (int) (paddingBottom * interpolatedTime);
      view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(),
          currentBottomPadding);
    }
    view.requestLayout();
  }

  public void setPaddingBottom(int paddingBottom) {
    this.paddingBottom = paddingBottom;
  }
}
