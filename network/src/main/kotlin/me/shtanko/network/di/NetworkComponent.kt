package me.shtanko.network.di

import com.google.gson.*
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import me.shtanko.core.di.ToolsProvider
import me.shtanko.model.HitItemEntity
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
import java.lang.reflect.Type
import javax.inject.Singleton

interface NetworkProvider {
    fun provideNetworkClient(): NetworkClient
}

@Module
interface NetworkModule {
    @Binds
    fun bindsNetworkClient(impl: NetworkClientImpl): NetworkClient
}

class HitDeserializer : JsonDeserializer<HitItemEntity> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HitItemEntity {
        val jsonObject = json?.asJsonObject
        val largeImageURL = jsonObject?.get("largeImageURL")?.asString ?: ""
        val webformatHeight = jsonObject?.get("webformatHeight")?.asInt ?: 0
        val webformatWidth = jsonObject?.get("webformatWidth")?.asInt ?: 0
        val likes = jsonObject?.get("likes")?.asInt ?: 0
        val imageWidth = jsonObject?.get("imageWidth")?.asInt ?: 0
        val id = jsonObject?.get("id")?.asInt ?: 0
        val userId = jsonObject?.get("user_id")?.asInt ?: 0
        val views = jsonObject?.get("views")?.asInt ?: 0
        val comments = jsonObject?.get("comments")?.asInt ?: 0
        val pageURL = jsonObject?.get("pageURL")?.asString ?: ""
        val imageHeight = jsonObject?.get("imageHeight")?.asInt ?: 0
        val webformatURL = jsonObject?.get("webformatURL")?.asString ?: ""
        val type = jsonObject?.get("type")?.asString ?: ""
        val previewHeight = jsonObject?.get("previewHeight")?.asInt ?: 0
        val tags = jsonObject?.get("tags")?.asString ?: ""
        val downloads = jsonObject?.get("downloads")?.asInt ?: 0
        val user = jsonObject?.get("user")?.asString ?: ""
        val favorites = jsonObject?.get("favorites")?.asInt ?: 0
        val imageSize = jsonObject?.get("imageSize")?.asInt ?: 0
        val previewWidth = jsonObject?.get("previewWidth")?.asInt ?: 0
        val userImageURL = jsonObject?.get("userImageURL")?.asString ?: ""
        val previewURL = jsonObject?.get("previewURL")?.asString ?: ""

        return HitItemEntity(largeImageURL, webformatHeight,
                webformatWidth, likes, imageWidth, id, userId, views, comments,
                pageURL, imageHeight, webformatURL, type, previewHeight, tags,
                downloads, user, favorites, imageSize, previewWidth,
                userImageURL, previewURL)
    }
}

@Module
object RestModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(HitItemEntity::class.java, HitDeserializer())
                .setDateFormat(GSON_DATE_FORMAT)
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