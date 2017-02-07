package com.valyakinaleksey.roleplayingsystem.utils.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CollapseAnimation extends Animation {


    private View view;
    private int initialHeight;
    private int initialBottomPadding;


    public CollapseAnimation(View view) {
        this.view = view;
        this.initialHeight = view.getHeight();
        this.initialBottomPadding = view.getPaddingBottom();
        setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = 1;
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), 0);
                view.requestLayout();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = initialHeight - (int) (initialHeight * interpolatedTime);
        int currentPadding = (int) (initialBottomPadding - initialBottomPadding * interpolatedTime);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), currentPadding);
        view.requestLayout();
    }


}
