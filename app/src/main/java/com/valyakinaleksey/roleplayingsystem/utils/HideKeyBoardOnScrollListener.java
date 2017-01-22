package com.valyakinaleksey.roleplayingsystem.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

public class HideKeyBoardOnScrollListener extends RecyclerView.OnScrollListener {
    public static final int MIN_DELTA = 5;
    private int minDelta = MIN_DELTA;
    private boolean startMove = false;

    public HideKeyBoardOnScrollListener() {
    }

    public HideKeyBoardOnScrollListener(int minDelta) {
        this.minDelta = minDelta;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            startMove = false;
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        if (!startMove && (dx >= minDelta || dy >= minDelta)) {
            startMove = true;
            KeyboardUtils.hideKeyboard((Activity) recyclerView.getContext());
        }
        super.onScrolled(recyclerView, dx, dy);
    }
}
      