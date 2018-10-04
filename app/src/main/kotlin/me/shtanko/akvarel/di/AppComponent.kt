package me.shtanko.akvarel.di

import dagger.Component
import me.shtanko.akvarel.Akvarel
import me.shtanko.collection.di.RepositoryRepoComponent
import me.shtanko.core.di.ActivityScope
import me.shtanko.core.di.ApplicationProvider
import me.shtanko.core.di.RepositoryProvider
import me.shtanko.core.di.ToolsProvider

@ActivityScope
@Component(
        dependencies = [ToolsProvider::class, RepositoryProvider::class],
        modules = [BindingsModule::class]
)
interface AppComponent : ApplicationProvider {

    fun inject(app: Akvarel)

    class Initializer private constructor() {
        companion object {
            fun init(app: Akvarel): AppComponent {

                val toolsProvider: ToolsProvider = ToolsComponent.Initializer.init(app)

                val collectionProvider = RepositoryRepoComponent.Initializer.init(toolsProvider)

                return DaggerAppComponent.builder()
                        .collectionProvider(collectionProvider)
                        .toolsProvider(toolsProvider)
                        .build()
            }
        }
    }

}

@dagger.Module
private abstract class BindingsModule {

}