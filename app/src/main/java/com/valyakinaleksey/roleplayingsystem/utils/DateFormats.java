package com.valyakinaleksey.roleplayingsystem.utils;

import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateFormats {

  public static final DateTimeFormatter dayMonthTime = DateTimeFormat.forPattern("dd.MM HH:mm");

  public static final DateTimeFormatter dayMonthYear = DateTimeFormat.forPattern("dd.MM.yyyy");

  public static final DateTimeFormatter isoTimestampNoMillisNoZone =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZoneUTC();

  public static final DateTimeFormatter dateNoMillisZoneDefault =
      DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.getDefault());

  public static final DateTimeFormatter hourMinutes = DateTimeFormat.forPattern("HH:mm");

  public static final DateTimeFormatter xmlFormat =
      DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.getDefault());

  public static final DateTimeFormatter dayMonthYearTime =
      DateTimeFormat.forPattern("dd.MM.yyyy HH:mm").withZone(DateTimeZone.getDefault());

  public static final DateTimeFormatter dayMonthYearTimeWithSeconds =
      DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss").withZone(DateTimeZone.getDefault());

  public static final DateTimeFormatter timeMinute = DateTimeFormat.forPattern("mm:ss");

  public static final DateTimeFormatter hourMinuteSecond = DateTimeFormat.forPattern("HH:mm:ss");

  public static final DateTimeFormatter clock = DateTimeFormat.forPattern("H:mm");

  public static final DateTimeFormatter timeDayMonth = DateTimeFormat.forPattern("HH:mm, dd MMM");

  public static final DateTimeFormatter dayMonth = DateTimeFormat.forPattern("d MMM");

  public static final DateTimeFormatter dayMonthFull = DateTimeFormat.forPattern("d MMMM");

  public static final DateTimeFormatter dayMonthYearTimeHuman =
      DateTimeFormat.forPattern("HH:mm dd\u00A0MMMM\u00A0yyyy");

  public static final DateTimeFormatter enUsFormat = ISODateTimeFormat.basicDate();

  public static final DateTimeFormatter iso8601Format =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZoneUTC();

  public static final DateTimeFormatter iso8601FormatMilli =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZoneUTC();

  public static final DateTimeFormatter iso8601FormatMilli6 =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").withZoneUTC();

  public static final DateTimeFormatter iso8601FormatTimezone =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZoneUTC();

  public static final DateTimeFormatter dayMonthShort = DateTimeFormat.forPattern("dd.MM");

  /**
   * Gets the default <code>TimeZone</code> for this host.
   * The source of the default <code>TimeZone</code>
   * may vary with implementation.
   *
   * @return a default <code>TimeZone</code>.
   */
  public static DateTimeZone getLocalTimeZone() {
    return DateTimeZone.forTimeZone(TimeZone.getDefault());
  }
}
