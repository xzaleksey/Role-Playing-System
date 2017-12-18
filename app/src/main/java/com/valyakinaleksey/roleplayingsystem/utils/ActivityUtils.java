package com.valyakinaleksey.roleplayingsystem.utils;

import android.app.Activity;
import android.graphics.Rect;
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
