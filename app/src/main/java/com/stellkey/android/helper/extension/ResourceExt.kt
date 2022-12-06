package com.stellkey.android.helper.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * Convert [String] to HexColor of [Int]
 */
val String.asColor: Int get() = Color.parseColor(this)

/**
 * Return Device Width Pixels of [Int], will return null when [Context] is Null
 */
val Context?.deviceWidth: Int get() = this?.resources?.displayMetrics?.widthPixels.orEmpty

/**
 * Return Device Height Pixels of [Int], will return null when [Context] is Null
 */
val Context?.deviceHeight: Int get() = this?.resources?.displayMetrics?.heightPixels.orEmpty

/**
 * Return Font/Typeface from res directory, will return null when [Context] is Null
 * @return [Typeface]
 */
fun Context?.font(@FontRes fontRes: Int) =
    this?.let { ResourcesCompat.getFont(it, fontRes) }

/**
 * Return Image/Drawable from res directory, will return null when [Context] is Null
 * @return [Drawable]
 */
fun Context?.drawable(@DrawableRes drawableRes: Int) =
    this?.let { ContextCompat.getDrawable(it, drawableRes) }

/**
 * Return Color from res directory of type [Int], will return null when [Context] is Null
 * @return Color[Int]
 */
fun Context?.color(@ColorRes colorRes: Int) =
    this?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

/**
 * Return Gradient/ColorStateList from res directory, will return null when [Context] is Null
 * @return [ColorStateList]
 */
fun Context?.colorStateList(@ColorRes colorRes: Int) =
    this?.let { ContextCompat.getColorStateList(it, colorRes) }

/**
 * Return Dimension pixel from res directory, will return null when [Context] is Null
 * @return [Int]
 */
fun Context?.dimen(@DimenRes dimenRes: Int) =
    this?.resources?.getDimension(dimenRes)