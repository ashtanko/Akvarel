package me.shtanko.network.api

import io.reactivex.Single
import io.shtanko.model.Hit
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayService {

  @GET("/api/")
  fun photos(
    @Query("q") query: String = "",
    @Query("lang") lang: String = "en",
    @Query("image_type") type: String = "all",
    @Query("orientation") orientation: String = "all",
    @Query("category") category: String? = null,
    @Query("min_width") minWidth: Int = 0,
    @Query("min_height") minHeight: Int = 0,
    @Query("colors") colors: String = "",
    @Query("editors_choice") editorsChoice: Boolean = false,
    @Query("safesearch") safeSearch: Boolean = false,
    @Query("order") order: String = "popular",
    @Query("page") page: Int = 1,
    @Query("per_page") perPage: Int = 5
  ): Single<Hit>

}