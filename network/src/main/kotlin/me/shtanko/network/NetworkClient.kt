package me.shtanko.network

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.shtanko.model.HitEntity
import me.shtanko.network.api.PixabayService
import javax.inject.Inject

interface NetworkClient {
    fun get(): Single<HitEntity>
}

class UserException(
        override val message: String?,
        cause: Throwable?
) : Throwable(message, cause) {
    constructor(message: String?) : this(message, null)
}

val JSON_PARAMS = object : ArrayList<Pair<String, String>>() {
    init {
        add(Pair("alt", "json"))
    }
}

class NetworkClientImpl @Inject constructor(
        private val service: PixabayService
) : NetworkClient {

    override fun get(): Single<HitEntity> {
        return service.photos()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }

}