package me.shtanko.akvarel.installed.di

import dagger.Component
import me.shtanko.akvarel.installed.Akvarel
import me.shtanko.core.di.ActivityScope
import me.shtanko.core.di.ApplicationProvider
import me.shtanko.core.di.ToolsProvider

@ActivityScope
@Component(
    dependencies = [ToolsProvider::class],
    modules = [BindingsModule::class]
)
interface AppComponent : ApplicationProvider {

  fun inject(app: Akvarel)

  class Initializer private constructor() {
    companion object {
      fun init(app: Akvarel): AppComponent {
        val toolsProvider: ToolsProvider = ToolsComponent.Initializer.init(app)
        return DaggerAppComponent.builder()
            .toolsProvider(toolsProvider)
            .build()
      }
    }
  }

}

@dagger.Module
private abstract class BindingsModule {

}