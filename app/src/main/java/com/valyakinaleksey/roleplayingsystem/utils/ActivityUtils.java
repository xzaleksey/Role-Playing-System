package com.valyakinaleksey.roleplayingsystem.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * TODO add description
 */
public class ActivityUtils {

    public static void blockControls(Activity activity, boolean flag) {
        if (flag) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public static void clearFocus(Activity activity) {
        View current = activity.getCurrentFocus();
        if (current != null) {
            current.clearFocus();
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    public static Rect locateView(View v) {
        int[] locs = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(locs);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = locs[0];
        location.top = locs[1];
        location.right = location.left + v.getMeasuredWidth();
        location.bottom = location.top + v.getMeasuredHeight();
        return location;
    }
}
