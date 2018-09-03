package me.shtanko.network

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.shtanko.network.api.PixabayService
import me.shtanko.network.data.Hit
import javax.inject.Inject

interface NetworkClient {
  fun get(): Single<Hit>
}

class NetworkClientImpl @Inject constructor(
  private val service: PixabayService
) : NetworkClient {

  override fun get(): Single<Hit> {
    return service.photos()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
  }

}