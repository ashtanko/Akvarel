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

package me.shtanko.categories.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import me.shtanko.categories.CategoriesFragment
import me.shtanko.categories.CategoriesRepository
import me.shtanko.categories.CategoriesRepositoryImpl
import me.shtanko.categories.CategoriesUseCase
import me.shtanko.categories.CategoriesUseCaseImpl
import me.shtanko.categories.CategoriesViewModel
import me.shtanko.common.di.ViewModelKey
import me.shtanko.common.viewmodel.ViewModelFactory
import me.shtanko.core.di.ApplicationProvider
import me.shtanko.core.di.FragmentScope

@Module
interface CategoriesModule {
  @Binds
  @IntoMap
  @ViewModelKey(CategoriesViewModel::class)
  abstract fun bindsCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel

  @Binds
  abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  fun bindsCollectionRepository(impl: CategoriesRepositoryImpl): CategoriesRepository

  @Binds
  fun bindsCategoriesUseCase(impl: CategoriesUseCaseImpl): CategoriesUseCase
}

@Component(
    dependencies = [ApplicationProvider::class],
    modules = [CategoriesModule::class]
)
@FragmentScope
interface CategoriesComponent {
  fun inject(fragment: CategoriesFragment)

  class Initializer private constructor() {
    companion object {
      fun init(
        applicationProvider: ApplicationProvider
      ): CategoriesComponent {
        return DaggerCategoriesComponent.builder()
            .applicationProvider(applicationProvider)
            .build()
      }
    }
  }
}