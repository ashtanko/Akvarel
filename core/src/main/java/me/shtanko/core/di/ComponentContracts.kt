package me.shtanko.core.di

import androidx.lifecycle.ViewModelProvider
import me.shtanko.core.App
import me.shtanko.core.Logger

interface ApplicationProvider : ToolsProvider

interface ToolsProvider {
  fun provideContext(): App
  fun provideLogger(): Logger
  fun provideViewModelFactory(): ViewModelProvider.Factory
}

interface AboutProvider