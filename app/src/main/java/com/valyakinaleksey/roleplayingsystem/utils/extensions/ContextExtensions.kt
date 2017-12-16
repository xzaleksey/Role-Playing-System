@file:JvmName("ContextExtensions")

package com.valyakinaleksey.roleplayingsystem.utils.extensions

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import com.valyakinaleksey.roleplayingsystem.di.app.RpsApp

private const val DEFAULT_TOOLBAR_HEIGHT = 56f
private const val DEFAULT_STATUS_BAR_HEIGHT_NEW = 24
private const val DEFAULT_STATUS_BAR_HEIGHT_OLD = 25

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    }
    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        DEFAULT_STATUS_BAR_HEIGHT_NEW.convertDpToPixel()
    } else {
        DEFAULT_STATUS_BAR_HEIGHT_OLD.convertDpToPixel()
    }
    return Math.ceil(result.toDouble()).toInt()
}

fun Context.getToolBarHeight(): Int {
    val resources = resources
    val resourceId = resources.getIdentifier("action_bar_size", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        DEFAULT_TOOLBAR_HEIGHT.convertDpToPixel()
    }
}

fun Float.convertDpToPixel(): Int {
    val resources = RpsApp.app().resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun Float.convertPixelsToDp(): Int {
    val resources = RpsApp.app().resources
    val metrics = resources.displayMetrics
    return (this / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Int.convertDpToPixel(): Int {
    val resources = RpsApp.app().resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Int.convertPixelsToDp(): Int {
    val resources = RpsApp.app().resources
    val metrics = resources.displayMetrics
    return (this / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}