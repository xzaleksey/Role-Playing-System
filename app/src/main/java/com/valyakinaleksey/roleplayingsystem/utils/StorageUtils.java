package com.valyakinaleksey.roleplayingsystem.utils;

import android.content.ContentResolver;
import android.net.Uri;
import com.valyakinaleksey.roleplayingsystem.core.utils.CompatUtils;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

import java.io.File;

public class StorageUtils {

    private static String getCacheDirectory(String dir) {
        final String externalDir = "Android/data/" + RpsApp.app().getPackageName();
        String resourcesDirectory = CompatUtils.getRootPath(RpsApp.app(), externalDir);
        return resourcesDirectory + "/" + dir;
    }

    public static String getCacheDirectory() {
        return getCacheDirectory(PathManager.CACHE_DIR);
    }

    public static String getImagesCacheDirectory() {
        return PathManager.IMAGE_DIR;
    }

    public static Uri resourceToUri(int resID) {
        RpsApp app = RpsApp.app();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                app.getResources().getResourcePackageName(resID) + '/' +
                app.getResources().getResourceTypeName(resID) + '/' +
                app.getResources().getResourceEntryName(resID));
    }

    public static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }

    public static void deleteCacheFolder() {
        deleteRecursive(new File(getCacheDirectory()));
    }
}
      