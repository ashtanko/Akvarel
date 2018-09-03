package me.shtanko.network.api

import me.shtanko.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Chain): Response {
    val originalRequest = chain.request()
    var request = originalRequest

    val url = request.url()
        .newBuilder()
        .addQueryParameter("key", BuildConfig.API_KEY)
        .build()

    request = request.newBuilder()
        .url(url)
        .build()
    return chain.proceed(request)

  }
}