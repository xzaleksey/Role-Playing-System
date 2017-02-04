package com.valyakinaleksey.roleplayingsystem.utils;

import android.content.ContentResolver;
import android.net.Uri;
import com.valyakinaleksey.roleplayingsystem.core.utils.CompatUtils;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

public class StorageUtils {

  public static String getCacheDirectory() {
    final String externalDir = "Android/data/" + RpsApp.app().getPackageName();
    String resourcesDirectory = CompatUtils.getRootPath(RpsApp.app(), externalDir);
    return resourcesDirectory + "/" + PathManager.CACHE_DIR;
  }

  public static Uri resourceToUri(int resID) {
    RpsApp app = RpsApp.app();
    return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
        app.getResources().getResourcePackageName(resID) + '/' +
        app.getResources().getResourceTypeName(resID) + '/' +
        app.getResources().getResourceEntryName(resID));
  }
}
      