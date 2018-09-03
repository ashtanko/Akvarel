package me.shtanko.akvarel.installed.tools

import android.util.Log
import me.shtanko.core.Logger

object Ours : Logger {

  private const val TAG: String = "AKVAREL_LOG"

  override fun d(vararg variables: Any) {
    Log.d(TAG, variables.toString())
  }
}