package com.stellkey.android.helper.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.annotation.ColorRes
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.stellkey.android.R
import timber.log.Timber
import java.io.IOException


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

fun Bitmap.scanQRImage(): String? {
    var contents: String? = null
    val intArray = IntArray(width * height)
    //copy pixel data from the Bitmap into the 'intArray' array
    getPixels(intArray, 0, width, 0, 0, width, height)
    val source: LuminanceSource = RGBLuminanceSource(width, height, intArray)
    val bitmap = BinaryBitmap(HybridBinarizer(source))
    val reader: Reader = MultiFormatReader()
    try {
        val result = reader.decode(bitmap)
        contents = result.text
    } catch (e: Exception) {
        Timber.e(e)
    }
    return contents
}

@Throws(IOException::class)
fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
        uri, "r"
    )
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return if (image.height <= 700) {
        image
    } else resizeRatioUsingHeight(
        image,
        700
    )
}

fun resizeRatioUsingHeight(bitmap: Bitmap, maxHeight: Int): Bitmap? {
    var imageWidth = bitmap.width
    var imageHeight = bitmap.height
    val bitmapRatio = imageWidth.toFloat() / imageHeight.toFloat()
    imageHeight = maxHeight
    imageWidth = (imageHeight * bitmapRatio).toInt()
    return Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, true)
}