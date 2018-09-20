/*
 * MIT License
 *
 * Copyright (c) 2018 Alexey Shtanko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.shtanko.akvarel

import android.app.Application
import android.os.StrictMode
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import me.shtanko.akvarel.di.AppComponent
import me.shtanko.core.App
import me.shtanko.core.Logger
import me.shtanko.core.di.ApplicationProvider
import javax.inject.Inject

class Akvarel : Application(), App {

    val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this@Akvarel) }

    @Inject
    lateinit var logger: Logger

    override fun onCreate() {
        strictMode()
        super.onCreate()
        appComponent.inject(this)
        //logger.startPoint()
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
    }

    override fun getAppComponent(): ApplicationProvider = appComponent

    private fun strictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build()
            )
            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .detectLeakedClosableObjects()
                            .detectLeakedClosableObjects()
                            .detectActivityLeaks()
                            .penaltyLog()
                            .penaltyDeath()
                            .build()
            )
        }
    }
}