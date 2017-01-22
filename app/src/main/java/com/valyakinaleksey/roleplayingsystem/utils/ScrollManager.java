package com.valyakinaleksey.roleplayingsystem.utils;

import android.support.v7.widget.RecyclerView;

public class ScrollManager {

  private RecyclerView recyclerView;

  public ScrollManager(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  public void smoothScrollToPosition(int pos) {
    recyclerView.smoothScrollToPosition(pos);
  }
}
      