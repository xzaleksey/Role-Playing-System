package com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.valyakinaleksey.roleplayingsystem.R;
import timber.log.Timber;

/**
 * Base class for Services that keep track of the number of active jobs and self-stop when the
 * count is zero.
 */
public abstract class MyBaseTaskService extends Service {

  private int mNumTasks = 0;

  public void taskStarted() {
    changeNumberOfTasks(1);
  }

  public void taskCompleted() {
    changeNumberOfTasks(-1);
  }

  private synchronized void changeNumberOfTasks(int delta) {
    Timber.d("changeNumberOfTasks:" + mNumTasks + ":" + delta);
    mNumTasks += delta;

    // If there are no tasks left, stop the service
    if (mNumTasks <= 0) {
      Timber.d("stopping");
      stopSelf();
    }
  }
}
      