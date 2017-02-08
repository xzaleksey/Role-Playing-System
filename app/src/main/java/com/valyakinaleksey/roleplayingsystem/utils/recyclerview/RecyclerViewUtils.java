package com.valyakinaleksey.roleplayingsystem.utils.recyclerview;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class RecyclerViewUtils {

  public static void checkFabShow(RecyclerView recyclerView, FloatingActionButton fab) {
    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    if (layoutManager instanceof LinearLayoutManager) {
      LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
      if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
          && linearLayoutManager.findLastCompletelyVisibleItemPosition()
          == recyclerView.getAdapter().getItemCount() - 1) {
        fab.show();
      }
    }
  }
}
      