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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.shtanko.collection.di.CollectionComponent
import me.shtanko.common.extensions.observe
import me.shtanko.common.extensions.viewModel
import me.shtanko.common.ui.BaseFragment
import me.shtanko.core.Logger
import me.shtanko.core.appComponent
import me.shtanko.model.HitItem
import javax.inject.Inject

fun CollectionFragment.provideInjection() {
    CollectionComponent.Initializer.init(appComponent)
        .inject(this)
}

class CollectionFragment : BaseFragment() {

    companion object {
        val instance = CollectionFragment()
    }

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var collectionViewModel: CollectionViewModel

    private lateinit var collectionAdapter: CollectionAdapter

    private var categories: RecyclerView? = null


    private var loading = false
    private var pageNumber = 1
    private var lastVisibleItem = 0
    private var totalItemCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideInjection()
        collectionViewModel = viewModel(viewModelFactory) {
            observe(data, ::handleRun)
            observe(items, ::handleItems)
            observe(isLoading, ::handleLoading)
        }
    }

    private fun handleLoading(isLoading: Boolean?) {
        isLoading?.let {
            this.loading = it
        }
    }

    private fun handleItems(items: List<HitItem>?) {
        logger.d("items: ", items?.size, items)
        val list = items?.map(HitItem::largeImageURL)
        list?.let {
            collectionAdapter.add(it)
            //collectionAdapter.categories = it
        }
    }

    private fun handleRun(command: CollectionViewModel.Command?) {
        command?.let {
            logger.d("command: ", command, it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(activity)
            .inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionAdapter = CollectionAdapter(activity)
        collectionViewModel.getItems()

        categories = activity?.findViewById<RecyclerView>(R.id.collection)
        categories?.also {
            it.layoutManager = LinearLayoutManager(activity)
            //it.setHasFixedSize(true)
            it.adapter = collectionAdapter
        }

        subscribeToLoadMore()
    }


    private fun subscribeToLoadMore() {
        categories?.let {
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = it.layoutManager as LinearLayoutManager
                    totalItemCount = layoutManager.itemCount
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    logger.d("addOnScrollListener: ", loading, pageNumber, totalItemCount, lastVisibleItem)

                    if (!loading && totalItemCount <= (lastVisibleItem + 1)) {
                        pageNumber++
                        collectionViewModel.onNextPage(pageNumber)
                        loading = true
                    }

                }
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        collectionViewModel.onDestroy()
    }
}