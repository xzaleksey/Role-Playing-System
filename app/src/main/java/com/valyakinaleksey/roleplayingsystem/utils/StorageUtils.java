package com.valyakinaleksey.roleplayingsystem.utils;

import com.valyakinaleksey.roleplayingsystem.core.utils.CompatUtils;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

public class StorageUtils {

  public static String getCacheDirectory() {
    final String externalDir = "Android/data/" + RpsApp.app().getPackageName();
    String resourcesDirectory = CompatUtils.getRootPath(RpsApp.app(), externalDir);
    return resourcesDirectory + "/" + PathManager.CACHE_DIR;
  }
}
      