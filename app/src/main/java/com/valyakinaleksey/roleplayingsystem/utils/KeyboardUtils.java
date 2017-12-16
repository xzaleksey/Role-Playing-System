package com.valyakinaleksey.roleplayingsystem.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void hideKeyboard(Activity activity, int delay) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                view.postDelayed(() -> {
                    InputMethodManager imm =
                            (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }, delay);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        hideKeyboard(activity, 10);
    }

    public static void forceHideKeyboard(Activity activity) {
        hideKeyboard(activity, 0);
    }

    public static void showSoftKeyboard(View view) {
        showSoftKeyboard(view, 10);
    }

    public static void showSoftKeyboard(View view, long delay) {
        if (view != null) {
            Context context = view.getContext();
            if (view.requestFocus()) {
                view.postDelayed(() -> {
                    InputMethodManager imm =
                            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                    }
                }, delay);
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            }
        }
    }

    public static void hideKeyboardNotEditText(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null && !(view instanceof android.widget.EditText)) {
                InputMethodManager imm =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
      