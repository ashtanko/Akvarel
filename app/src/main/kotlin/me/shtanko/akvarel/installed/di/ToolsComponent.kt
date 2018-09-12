package me.shtanko.akvarel.installed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.*
import dagger.multibindings.IntoMap
import me.shtanko.akvarel.installed.tools.Ours
import me.shtanko.common.di.ViewModelKey
import me.shtanko.common.viewmodel.ViewModelFactory
import me.shtanko.core.App
import me.shtanko.core.Logger
import me.shtanko.core.di.ToolsProvider
import javax.inject.Inject
import javax.inject.Singleton

@Module
class ToolsModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideLogger(app: App): Logger {
            val instance = Ours
//      val context = app.getApplicationContext()
//
//      var dir = context.getExternalFilesDir(null)
//      if (dir == null) {
//        dir = context.cacheDir
//      }
//      val path = dir?.absolutePath
//      instance.logPath = path
            return instance
        }
    }

}

class MainViewModel @Inject constructor() : ViewModel() {

}

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Singleton
@Component(modules = [ToolsModule::class, ViewModelModule::class])
interface ToolsComponent : ToolsProvider {

    @Component.Builder
    interface Builder {
        fun build(): ToolsComponent
        @BindsInstance
        fun app(app: App): Builder
    }

    class Initializer private constructor() {
        companion object {
            fun init(app: App): ToolsProvider = DaggerToolsComponent.builder()
                    .app(app)
                    .build()
        }
    }

}