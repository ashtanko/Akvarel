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

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.processors.PublishProcessor
import me.shtanko.model.Hit
import me.shtanko.model.HitItem
import org.reactivestreams.Publisher
import javax.inject.Inject

class CollectionViewModel @Inject constructor(
    private val collectionUseCase: CollectionUseCase
) : ViewModel() {

    var data: MutableLiveData<CollectionViewModel.Command> = MutableLiveData()
    var items: MutableLiveData<List<HitItem>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val paginator = PublishProcessor.create<Int>()
    private var page = 1
    val compositeDisposable = CompositeDisposable()

    sealed class Command {
        object Run : Command()
    }

    @SuppressLint("CheckResult", "LogNotTimber")
    fun getItems() {
        subscribeForData()
    }

    fun onNextPage(pageNumber: Int) {
        this.page = pageNumber
        paginator.onNext(pageNumber)
        Log.d("AKVAREL", "onNextPage: $page")
    }

    private fun subscribeForData() {
        val disposable = paginator.onBackpressureDrop().concatMap(Function<Int, Publisher<Hit>> { page ->
            isLoading.value = true
            return@Function collectionUseCase.loadCollection(page)
        }).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isLoading.value = false
                items.value = it.items
            }, {
                it.printStackTrace()
                Log.d("AKVAREL", "ERROR: ${it.localizedMessage}")
            })

        compositeDisposable.add(disposable)

        paginator.onNext(page)
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    init {
        data.value = Command.Run
        //subscribeForData()
    }

}