package me.shtanko.akvarel.installed

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import me.shtanko.common.extensions.instantIntent

object ActivityLaunchHelper {

  private const val URL_BASE = "https://shtanko.me"
  private const val URL_CATEGORIES = "$URL_BASE/categories"

  fun categorySelectionIntent(context: Context? = null) = instantIntent(
      URL_CATEGORIES, context
  )

  fun launchCategories(
    activity: Activity,
    options: ActivityOptionsCompat? = null
  ) {
    val starter = categorySelectionIntent(activity)
    if (options == null) {
      activity.startActivity(starter)
    } else {
      ActivityCompat.startActivity(activity, starter, options.toBundle())
    }
  }

  fun launchAbout(activity: Activity) {
  }

}