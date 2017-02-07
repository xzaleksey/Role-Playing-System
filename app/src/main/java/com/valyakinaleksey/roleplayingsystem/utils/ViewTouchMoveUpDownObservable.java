package com.valyakinaleksey.roleplayingsystem.utils;

import android.view.MotionEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

public class ViewTouchMoveUpDownObservable implements Observable.OnSubscribe<Integer> {

  private boolean startMove = false;

  private final View view;
  private int initialY;
  private int delta = 10;
  private boolean handled = false;

  public ViewTouchMoveUpDownObservable(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    view.setOnTouchListener((v, event) -> {
      if (subscriber != null) {
        if (!subscriber.isUnsubscribed()) {
          switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
              if (!handled) {
                if (startMove) {
                  float dif = event.getY() - initialY;
                  if (Math.abs(dif) > delta) {
                    handled = true;
                    if (dif < 0) {
                      subscriber.onNext(1);
                    } else {
                      subscriber.onNext(-1);
                    }
                  }
                } else {
                  initialY = (int) event.getY();
                  startMove = true;
                }
              }
              break;
            default:
              handled = false;
              startMove = false;
          }
        }
      }
      return false;
    });

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnTouchListener(null);
      }
    });
  }
}