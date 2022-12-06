package com.stellkey.android.helper.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorRes
import com.stellkey.android.R

fun Context.createCircleDrawable(
    whSize: Pair<Int, Int>,
    @ColorRes backgroundColor: Int = R.color.light_black
): GradientDrawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        color = colorStateList(backgroundColor)
        setSize(whSize.first, whSize.second)
    }
}

fun Bitmap?.getScaledBitmapAtLongestSide(
    targetSize: Int
): Bitmap? {
    if (this == null || width <= targetSize && height <= targetSize) return this

    val targetWidth: Int
    val targetHeight: Int

    when {
        height > width -> {
            targetHeight = targetSize
            val percentage = targetSize.toFloat() / height
            targetWidth = (width * percentage).toInt()
        }
        else -> {
            targetWidth = targetSize
            val percentage = targetSize.toFloat() / width
            targetHeight = (height * percentage).toInt()
        }
    }

    return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
}