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

package me.shtanko.model

import com.google.gson.annotations.SerializedName

data class Hit(
        val total: Int = 0
)

data class HitItem(
        val largeImageURL: String = ""
)

data class HitEntity(
        @SerializedName("totalHits") val totalHits: Int = 0,
        @SerializedName("hits") val hits: List<HitItemEntity>,
        @SerializedName("total") val total: Int = 0
) {

    fun toHit(): Hit {
        return Hit(totalHits)
    }

    override fun toString(): String {
        return "HitEntity(totalHits=$totalHits, hits=$hits, total=$total)"
    }
}

data class HitItemEntity(
        @SerializedName("largeImageURL") val largeImageURL: String,
        @SerializedName("webformatHeight") val webformatHeight: Int = 0,
        @SerializedName("webformatWidth") val webformatWidth: Int = 0,
        @SerializedName("likes") val likes: Int = 0,
        @SerializedName("imageWidth") val imageWidth: Int = 0,
        @SerializedName("id") val id: Int? = 0,
        @SerializedName("user_id") val userId: Int = 0,
        @SerializedName("views") val views: Int = 0,
        @SerializedName("comments") val comments: Int = 0,
        @SerializedName("pageURL") val pageURL: String = "",
        @SerializedName("imageHeight") val imageHeight: Int = 0,
        @SerializedName("webformatURL") val webformatURL: String = "",
        @SerializedName("type") val type: String = "",
        @SerializedName("previewHeight") val previewHeight: Int = 0,
        @SerializedName("tags") val tags: String = "",
        @SerializedName("downloads") val downloads: Int = 0,
        @SerializedName("user") val user: String = "",
        @SerializedName("favorites") val favorites: Int = 0,
        @SerializedName("imageSize") val imageSize: Int = 0,
        @SerializedName("previewWidth") val previewWidth: Int = 0,
        @SerializedName("userImageURL") val userImageURL: String = "",
        @SerializedName("previewURL") val previewURL: String = ""
) {

    fun toHitItem(): HitItem {
        return HitItem(largeImageURL)
    }

    override fun toString(): String {
        return "HitItemEntity(largeImageURL='$largeImageURL', webformatHeight=$webformatHeight, webformatWidth=$webformatWidth, likes=$likes, imageWidth=$imageWidth, id=$id, userId=$userId, views=$views, comments=$comments, pageURL='$pageURL', imageHeight=$imageHeight, webformatURL='$webformatURL', type='$type', previewHeight=$previewHeight, tags='$tags', downloads=$downloads, user='$user', favorites=$favorites, imageSize=$imageSize, previewWidth=$previewWidth, userImageURL='$userImageURL', previewURL='$previewURL')"
    }
}


