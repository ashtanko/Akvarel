package me.shtanko.network

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.shtanko.model.HitEntity
import me.shtanko.network.api.PixabayService
import javax.inject.Inject

interface NetworkClient {
    fun get(page: Int): Flowable<HitEntity>
}

class NetworkClientImpl @Inject constructor(
        private val service: PixabayService
) : NetworkClient {

    override fun get(page: Int): Flowable<HitEntity> {
        return service.photos(perPage = 25, page = page)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }

}