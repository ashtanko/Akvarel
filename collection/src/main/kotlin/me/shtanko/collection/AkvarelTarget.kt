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

package me.shtanko.collection

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import me.shtanko.common.extensions.ColorUtils
import me.shtanko.common.extensions.getBitmap

object DrawableUtils {

    private fun modifyAlpha(rgb: Int?, alpha: Double): Int {
        return ColorUtils.modifyAlpha(
            rgb
                ?: Color.rgb(80, 80, 80), alpha
        )
    }

    fun createRipple(
        palette: Palette?,
        @FloatRange(from = 0.0, to = 1.0) darkAlpha: Double,
        @FloatRange(from = 0.0, to = 1.0) lightAlpha: Double,
        @ColorInt fallbackColor: Int,
        bounded: Boolean
    ): RippleDrawable {
        var rippleColor = fallbackColor

        palette?.let {
            when {
                it.vibrantSwatch != null -> {
                    rippleColor = modifyAlpha(it.vibrantSwatch?.rgb, darkAlpha)
                }
                it.lightVibrantSwatch != null -> {
                    rippleColor = modifyAlpha(it.lightVibrantSwatch?.rgb, lightAlpha)
                }
                it.darkVibrantSwatch != null -> {
                    rippleColor = modifyAlpha(it.darkVibrantSwatch?.rgb, darkAlpha)
                }
                it.mutedSwatch != null -> {
                    rippleColor = modifyAlpha(it.mutedSwatch?.rgb, darkAlpha)
                }
                it.lightMutedSwatch != null -> {
                    rippleColor = modifyAlpha(it.lightMutedSwatch?.rgb, lightAlpha)
                }
                it.darkMutedSwatch != null -> {
                    rippleColor = modifyAlpha(it.darkMutedSwatch?.rgb, darkAlpha)
                }
            }

        }

        return RippleDrawable(
            ColorStateList.valueOf(rippleColor), null, if (bounded)
                ColorDrawable(Color.WHITE) else null
        )
    }
}

class AkvarelTarget(
    private val imageView: AppCompatImageView
) : DrawableImageViewTarget(imageView), Palette.PaletteAsyncListener {

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        val bitmap = resource.getBitmap() ?: return
        Palette.from(bitmap).clearFilters().generate(this)
    }

    override fun onGenerated(palette: Palette?) {
        //todo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.foreground = DrawableUtils.createRipple(
                palette,
                0.25,
                0.5,
                ContextCompat.getColor(view.context, R.color.mid_grey),
                true
            )
        }
    }

}