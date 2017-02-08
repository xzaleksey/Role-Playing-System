package com.valyakinaleksey.roleplayingsystem.utils.recyclerview.scroll;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.valyakinaleksey.roleplayingsystem.utils.recyclerview.RecyclerViewUtils;

public class HideFablListener extends RecyclerView.OnScrollListener {

  private FloatingActionButton fab;

  public HideFablListener(FloatingActionButton fab) {
    this.fab = fab;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    if (dy > 0) {
      // Scroll Down
      if (fab.isShown()) {
        fab.hide();
      }
    } else if (dy < 0) {
      // Scroll Up
      if (!fab.isShown()) {
        fab.show();
      }
    }
  }

  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    RecyclerViewUtils.checkFabShow(recyclerView, fab);
    super.onScrollStateChanged(recyclerView, newState);
  }
}
      