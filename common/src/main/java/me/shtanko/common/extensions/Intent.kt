package me.shtanko.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun instantIntent(
  url: String,
  context: Context? = null
): Intent {
  val intent = Intent(Intent.ACTION_VIEW, url.toUri())
      .addCategory(Intent.CATEGORY_DEFAULT)
      .addCategory(Intent.CATEGORY_BROWSABLE)
  if (context != null) {
    intent.`package` = context.packageName
  }
  return intent
}

private const val PACKAGE_NAME = "me.shtanko.akvarel"

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
  /**
   * The activity class name.
   */
  val className: String
}

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun intentTo(addressableActivity: AddressableActivity): Intent {
  return Intent(Intent.ACTION_VIEW).setClassName(
      PACKAGE_NAME,
      addressableActivity.className)
}

/**
 * AboutActivity
 */
object About : AddressableActivity {
  override val className = "$PACKAGE_NAME.about.AboutActivity"
}