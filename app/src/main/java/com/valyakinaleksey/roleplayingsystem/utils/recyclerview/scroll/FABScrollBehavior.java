package com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class FABScrollBehavior extends FloatingActionButton.Behavior {
  public FABScrollBehavior(Context context, AttributeSet attrs) {
    super();
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
      View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
      child.hide();
    } else if (dyConsumed < 0 && child.getVisibility() == View.GONE) {
      child.show();
    }
  }

  @Override public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child,
      View dependency) {
    return dependency instanceof RecyclerView;
  }

  @Override public boolean onStartNestedScroll(CoordinatorLayout parent, FloatingActionButton child,
      View directTargetChild, View target, int nestedScrollAxes) {
    return true;
  }
}
      