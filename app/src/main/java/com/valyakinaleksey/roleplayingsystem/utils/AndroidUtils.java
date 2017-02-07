package com.valyakinaleksey.roleplayingsystem.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import com.valyakinaleksey.roleplayingsystem.R;

public final class AndroidUtils {
  public static boolean isLollipop() {
    return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

  public static int getToolbarHeight(Context context) {
    final TypedArray styledAttributes =
        context.getTheme().obtainStyledAttributes(new int[] { R.attr.actionBarSize });
    int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
    styledAttributes.recycle();

    return toolbarHeight;
  }

  public static int getTabsHeight(Context context) {
    return (int) context.getResources().getDimension(R.dimen.custom_tab_layout_height);
  }
}
      