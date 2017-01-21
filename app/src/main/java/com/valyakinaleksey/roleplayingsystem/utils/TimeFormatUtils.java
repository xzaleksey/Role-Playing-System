package com.valyakinaleksey.roleplayingsystem.utils;

import android.text.format.DateFormat;
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeFormatUtils {
  public static final String H_MM_A = "h:mm a";
  public static final String H_MM = "H:mm";

  public static String getShortTime(DateTime dateTime) {
    return dateTime.toString(getTimeFormat());
  }

  public static boolean is24HourFormat() {
    return DateFormat.is24HourFormat(RpsApp.app());
  }

  public static DateTimeFormatter getTimeFormat() {
    if (is24HourFormat()) {
      return DateTimeFormat.forPattern(H_MM);
    } else {
      return DateTimeFormat.forPattern(H_MM_A);
    }
  }
}
      