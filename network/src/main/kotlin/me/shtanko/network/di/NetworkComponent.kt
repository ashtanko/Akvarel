package me.shtanko.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import me.shtanko.core.di.ToolsProvider
import me.shtanko.model.HitsItem
import me.shtanko.model.RuntimeTypeAdapterFactory
import me.shtanko.network.BuildConfig
import me.shtanko.network.GSON_DATE_FORMAT
import me.shtanko.network.NetworkClient
import me.shtanko.network.NetworkClientImpl
import me.shtanko.network.api.ApiKeyInterceptor
import me.shtanko.network.api.PixabayService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

interface NetworkProvider {
    fun provideNetworkClient(): NetworkClient
}

@Module
interface NetworkModule {
    @Binds
    fun bindsNetworkClient(impl: NetworkClientImpl): NetworkClient
}

@Module
object RestModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideGson(): Gson {

        val adapter = RuntimeTypeAdapterFactory
                .of(HitsItem::class.java)

        return GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat(GSON_DATE_FORMAT)
                .registerTypeAdapterFactory(adapter)
                .create()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(Level.BODY)

    @Provides
    @JvmStatic
    @Singleton
    fun provideKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

    @Provides
    @JvmStatic
    @Singleton
    fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            keyInterceptor: ApiKeyInterceptor
    ): OkHttpClient =
            OkHttpClient().newBuilder()
                    .addNetworkInterceptor(loggingInterceptor)
                    .addNetworkInterceptor(keyInterceptor)
                    .build()

    @Provides
    @JvmStatic
    @Singleton
    fun provideConvertersFactory(gson: Gson): Converter.Factory =
            GsonConverterFactory.create(gson)

    @Provides
    @JvmStatic
    @Singleton
    fun provideRetrofitBuilder(
            client: OkHttpClient,
            converterFactory: Converter.Factory
    ): Retrofit.Builder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(converterFactory)
            .client(client)

    @Provides
    @JvmStatic
    @Singleton
    fun provideApiService(builder: Retrofit.Builder): PixabayService = builder
            .baseUrl(BuildConfig.ENDPOINT)
            .build()
            .create(PixabayService::class.java)

}

@Singleton
@Component(
        dependencies = [
            ToolsProvider::class],
        modules = [NetworkModule::class, RestModule::class]
)
interface NetworkComponent : NetworkProvider {

}