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

package me.shtanko.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.shtanko.collection.R
import me.shtanko.collection.di.CollectionComponent
import me.shtanko.common.extensions.viewModel
import me.shtanko.common.ui.BaseFragment
import me.shtanko.core.App
import me.shtanko.core.Logger
import javax.inject.Inject

class CollectionFragment : BaseFragment() {

  companion object {
    val instance = CollectionFragment()
  }

  @Inject
  lateinit var logger: Logger

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var collectionViewModel: CollectionViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    CollectionComponent.Initializer.init((activity?.applicationContext as App).getAppComponent())
        .inject(this@CollectionFragment)

    collectionViewModel = viewModel(viewModelFactory) {

    }

    logger.d("EEEEE", collectionViewModel)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return LayoutInflater.from(activity)
        .inflate(R.layout.fragment_collection, container, false)
  }

}