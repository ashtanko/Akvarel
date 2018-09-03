package me.shtanko.akvarel.search.di

import dagger.Component
import me.shtanko.akvarel.search.SearchActivity
import me.shtanko.core.di.ToolsProvider
import me.shtanko.network.di.DaggerNetworkComponent
import me.shtanko.network.di.NetworkProvider

@Component(dependencies = [ToolsProvider::class, NetworkProvider::class])
interface SearchComponent {
  fun inject(activity: SearchActivity)

  class Initializer private constructor() {
    companion object {
      fun init(toolsProvider: ToolsProvider): SearchComponent {

        val networkComponent = DaggerNetworkComponent.builder()
            .toolsProvider(toolsProvider)
            .build()

        return DaggerSearchComponent.builder()
            .networkProvider(networkComponent)
            .toolsProvider(toolsProvider)
            .build()
      }
    }
  }
}