package me.shtanko.akvarel.tools

import android.util.Log
import me.shtanko.core.Logger
import java.util.*
import javax.inject.Inject

class LoggerImpl @Inject constructor(

) : Logger {

    private val TAG: String = "AKVAREL"

    override fun d(vararg variables: Any?) {
        Log.d(TAG, Arrays.toString(variables))
    }

    override fun startPoint() {

    }
}