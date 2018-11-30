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

package me.shtanko.collection.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import me.shtanko.collection.*
import me.shtanko.common.di.ViewModelKey
import me.shtanko.common.viewmodel.ViewModelFactory
import me.shtanko.core.collection.CollectionRepository
import me.shtanko.core.di.ApplicationProvider
import me.shtanko.core.di.FragmentScope
import me.shtanko.core.di.RepositoryProvider
import me.shtanko.core.di.ToolsProvider
import me.shtanko.network.di.DaggerNetworkComponent
import me.shtanko.network.di.NetworkProvider
import kotlin.text.Typography.dagger

@Module
interface CollectionModule {

    @FragmentScope
    @Binds
    fun bindsCollectionUseCase(impl: CollectionUseCaseImpl): CollectionUseCase

    @Binds
    @IntoMap
    @ViewModelKey(CollectionViewModel::class)
    abstract fun bindsCollectionViewModel(viewModel: CollectionViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}

@Module
interface CollectionRepoModule {

    @Binds
    fun bindsCollectionRepo(impl: CollectionRepositoryImpl): CollectionRepository
}

@Component(
    dependencies = [ToolsProvider::class, NetworkProvider::class],
    modules = [CollectionRepoModule::class]
)
interface RepositoryRepoComponent : RepositoryProvider {

    class Initializer private constructor() {
        companion object {
            fun init(toolsProvider: ToolsProvider): RepositoryProvider {

                val networkProvider = DaggerNetworkComponent.builder()
                    .toolsProvider(toolsProvider)
                    .build()

                return DaggerRepositoryRepoComponent.builder()
                    .toolsProvider(toolsProvider)
                    .networkProvider(networkProvider)
                    .build()

            }
        }
    }

}

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [CollectionModule::class]
)
@FragmentScope
interface CollectionComponent {

    fun inject(fragment: CollectionFragment)

    class Initializer private constructor() {
        companion object {
            fun init(
                applicationProvider: ApplicationProvider
            ): CollectionComponent {

                return DaggerCollectionComponent.builder()
                    .applicationProvider(applicationProvider)
                    .build()
            }
        }
    }
}