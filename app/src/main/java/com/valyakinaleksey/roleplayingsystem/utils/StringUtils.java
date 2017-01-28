package com.valyakinaleksey.roleplayingsystem.utils;

import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.FORMAT_SLASHES;

public class StringUtils {
  public static final String EMPTY_STRING = "";

  public static String getStringById(@StringRes int resId) {
    return RpsApp.app().getString(resId);
  }

  public static String getPluralById(@PluralsRes int resId, int count) {
    return RpsApp.app().getResources().getQuantityString(resId, count, count);
  }

  public static String formatWithCount(String s, int count) {
    return s + " (" + String.valueOf(count) + ")";
  }

  public static String formatWithSlashes(String s) {
    return String.format(FORMAT_SLASHES, s);
  }
}
      