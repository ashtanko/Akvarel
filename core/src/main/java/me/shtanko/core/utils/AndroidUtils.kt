package me.shtanko.core.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics

object AndroidUtils {

  @JvmField
  var density = 1f

  @JvmField
  var displaySize = Point()

  @JvmField
  var statusBarHeight = 0

  private val displayMetrics = DisplayMetrics()

  @JvmStatic
  fun dp(value: Float): Int {
    return if (value == 0f) {
      0
    } else Math.ceil(density.toDouble() * value).toInt()
  }

  @JvmStatic
  fun getPixelsInCM(
    cm: Float,
    isX: Boolean
  ): Float {
    return (cm / 2.54f) * (if (isX) displayMetrics.xdpi else displayMetrics.ydpi)
  }

  fun checkDisplaySize(
    context: Context,
    conf: Configuration
  ) {
    density = context.resources.displayMetrics.density
  }
}