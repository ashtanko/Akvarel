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

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Reader

data class Hit(
        @SerializedName("totalHits") val totalHits: Int = 0,
        @SerializedName("hits") val hits: List<HitsItem>,
        @SerializedName("total") val total: Int = 0
) {
    fun asJson() = {

        val listOfTestObject = object : TypeToken<List<HitsItem>>() {

        }.type
        Gson().toJson(this, listOfTestObject)

        /*
        Type listOfTestObject = new TypeToken<List<TestObject>>(){}.getType();
        String s = gson.toJson(list, listOfTestObject);
        List<TestObject> list2 = gson.fromJson(s, listOfTestObject);
         */

        //Gson().toJson(this)


    }

    object Deserializer : ResponseDeserializable<Hit> {
        override fun deserialize(reader: Reader): Hit? {
            println("FUEL_LOL Deserializer: $reader")
            return Gson().fromJson(reader, Hit::class.java)
        }
    }
}

data class HitsItem(
        @SerializedName("largeImageURL") val largeImageURL: String,
        @SerializedName("webformatHeight") val webformatHeight: Int = 0,
        @SerializedName("webformatWidth") val webformatWidth: Int = 0,
        @SerializedName("likes") val likes: Int = 0,
        @SerializedName("imageWidth") val imageWidth: Int = 0,
        @SerializedName("id") val id: Int = 0,
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

    init {

    }

    fun deserialize(json: String): List<HitsItem> {
        val type = object : TypeToken<List<HitsItem>>() {}.type
        return Gson().fromJson(json, type)
    }
}


