package com.valyakinaleksey.roleplayingsystem.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.view.ParentActivity;

import static com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage.MyUploadService.EXTRA_DOWNLOAD_URL;
import static com.valyakinaleksey.roleplayingsystem.data.repository.firebasestorage.MyUploadService.EXTRA_FILE_URI;
import static com.valyakinaleksey.roleplayingsystem.utils.StringUtils.getStringById;

public class NotificationUtils {

  /**
   * Show notification with a progress bar.
   */
  public static void showProgressNotification(int notifId, String caption, long completedUnits,
      long totalUnits) {
    int percentComplete = 0;

    if (totalUnits > 0) {
      percentComplete = (int) (100 * completedUnits / totalUnits);
    }

    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(RpsApp.app()).setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getStringById(R.string.app_name))
            .setContentText(caption)
            .setProgress(100, percentComplete, false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setAutoCancel(false);

    NotificationManager manager =
        (NotificationManager) RpsApp.app().getSystemService(Context.NOTIFICATION_SERVICE);

    manager.notify(notifId, builder.build());
  }

  public static void showFinishedNotification(int notifId, String caption, Intent intent,
      boolean success) {
    // Make PendingIntent for notification
    PendingIntent pendingIntent =
        PendingIntent.getActivity(RpsApp.app(), 0 /* requestCode */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    int icon = success ? R.drawable.ic_done_black_24dp : R.drawable.ic_add_black_24dp;

    android.support.v4.app.NotificationCompat.Builder builder =
        new android.support.v4.app.NotificationCompat.Builder(RpsApp.app()).setSmallIcon(icon)
            .setContentTitle(getStringById(R.string.app_name))
            .setContentText(caption)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

    NotificationManager manager =
        (NotificationManager) RpsApp.app().getSystemService(Context.NOTIFICATION_SERVICE);

    manager.notify(notifId, builder.build());
  }

  public static void dismissNotification(int notificationId) {
    NotificationManager manager =
        (NotificationManager) RpsApp.app().getSystemService(Context.NOTIFICATION_SERVICE);

    manager.cancel(notificationId);
  }

  private void showUploadFinishedNotification(int notifId, @Nullable Uri downloadUrl,
      @Nullable Uri fileUri) {
    Intent intent =
        new Intent(RpsApp.app(), ParentActivity.class).putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
            .putExtra(EXTRA_FILE_URI, fileUri)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    boolean success = downloadUrl != null;
    String caption = success ? getStringById(R.string.success) : getStringById(R.string.fail);
    showFinishedNotification(notifId, caption, intent, success);
  }
}
      