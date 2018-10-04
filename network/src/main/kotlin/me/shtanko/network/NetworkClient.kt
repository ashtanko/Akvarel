package me.shtanko.network

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.shtanko.model.Hit
import me.shtanko.network.api.PixabayService
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8

interface NetworkClient {
    fun get(): Single<Hit>
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

    override fun get(): Single<Hit> {

        Log.d("FUEL_LOL", "start")

        val request = "https://pixabay.com/api/?image_type=photos&key=9756721-ea7409ae29c32b6cf2472388e&pretty=true".httpGet(JSON_PARAMS)

        Log.d("FUEL_LOL", "request: $request")

        val rxObject = request.rx_object(Hit.Deserializer)

        Log.d("FUEL_LOL", "rxObject: $rxObject")

        val single = rxObject.flatMapMaybe {
            when (it) {
                is Result.Success -> {
                    Log.d("FUEL_LOL", "Success: $it")
                    Maybe.just(it.value)
                }

                is Result.Failure -> {
                    Log.d("FUEL_LOL", "Failure: $it")
                    return@flatMapMaybe try {
                        Log.d("FUEL_LOL", "TRY BLOCK: ${it.error}")
                        val error = Maybe.error<Hit>(
                                Gson().fromJson(
                                        it.error.response.data.toString(UTF_8),
                                        UserException::class.java

                                )
                        )
                        Log.d("FUEL_LOL", "TRY BLOCK CONTINUE: $error")
                        error
                    } catch (error: Throwable) {
                        Log.d("FUEL_LOL", "CATCH BLOCK: $error")
                        val error1 = Maybe.error<Hit>(UserException(it.error.message
                                ?: it.error.exception.message ?: it.error.response.responseMessage))
                        Log.d("FUEL_LOL", "CATCH BLOCK ERROR 1: $error1")
                        error1
                    }
                }

            }
        }.toSingle()

        return single


//        return service.photos()
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
    }

}