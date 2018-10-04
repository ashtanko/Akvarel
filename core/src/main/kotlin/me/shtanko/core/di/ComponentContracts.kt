package me.shtanko.core.di

import me.shtanko.core.App
import me.shtanko.core.Logger
import me.shtanko.core.collection.CollectionRepository

interface ApplicationProvider : ToolsProvider,
        RepositoryProvider

interface ToolsProvider {
    fun provideContext(): App
    fun provideLogger(): Logger
}

interface AboutProvider

interface RepositoryProvider {
    fun provideCollectionRepo(): CollectionRepository
}