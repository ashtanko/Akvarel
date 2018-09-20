package me.shtanko.akvarel.installed

import android.app.Application
import android.os.StrictMode
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import me.shtanko.akvarel.BuildConfig
import me.shtanko.akvarel.installed.di.AppComponent
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

        logger.startPoint()
        logger.d("LOL: $logger")

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