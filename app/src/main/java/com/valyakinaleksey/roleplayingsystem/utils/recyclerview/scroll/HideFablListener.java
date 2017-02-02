package com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

public class HideFablListener extends RecyclerView.OnScrollListener {

  private FloatingActionButton fab;

  public HideFablListener(FloatingActionButton fab) {
    this.fab = fab;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    if (dy > 0 || dy < 0 && fab.isShown()) {
      fab.hide();
    }
  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
      fab.show();
    }

    super.onScrollStateChanged(recyclerView, newState);
  }
}
      