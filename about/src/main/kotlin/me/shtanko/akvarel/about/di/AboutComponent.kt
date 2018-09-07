package me.shtanko.akvarel.about.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import me.shtanko.common.di.ViewModelKey
import me.shtanko.common.viewmodel.ViewModelFactory
import me.shtanko.core.di.AboutProvider
import me.shtanko.core.di.ToolsProvider
import javax.inject.Inject
import javax.inject.Named

class AboutViewModel @Inject constructor() : ViewModel() {

}

@Module
abstract class AboutViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(AboutViewModel::class)
  abstract fun bindsMainViewModel(viewModel: AboutViewModel): ViewModel

  @Binds
  @Named("About")
  abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
interface AboutModule {

}

@Component(
    dependencies = [ToolsProvider::class],
    modules = [AboutModule::class, AboutViewModelModule::class]
)
interface AboutComponent : AboutProvider {

  class Initializer private constructor() {
    companion object {
      fun init(toolsProvider: ToolsProvider): AboutProvider {
        return DaggerAboutComponent.builder()
            .toolsProvider(toolsProvider)
            .build()
      }

    }
  }
}