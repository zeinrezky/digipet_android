package com.stellkey.android.helper.extension

import android.util.Patterns

const val emptyString = ""
const val emptyInt = -1
const val emptyFloat = 0F
const val emptyLong = 0L
const val emptyDouble = 0.0
const val emptyBoolean = false

/**
 * Proceed and Return Non-Null Value of Nullable String
 * @return [String]
 */
val String?.orEmpty get() = orEmpty(emptyString)

/**
 * Proceed and Return Non-Null Value of Nullable CharSequence
 * @param default is the replacement of nullable value of any word in the CharSequence
 * @param condition is the condition which nullable can be replaced with non-null value
 * @return [CharSequence]
 */
fun CharSequence?.orEmpty(
    default: String = emptyString,
    condition: Regex? = null
): CharSequence {
    val regex = condition ?: Regex("^(null|NULL|Null|0)")
    return if(this?.contains(regex) == true) replace(regex, default) else this ?: default
}

/**
 * Proceed and Return Non-Null Value of Nullable CharSequence
 * @return [CharSequence]
 */
val CharSequence?.orEmpty get() = orEmpty(emptyString)

/**
 * Proceed and Return Non-Null Value of Nullable Long
 * @param default is the replacement of nullable value of Long
 * @return [Long]
 */
fun Long?.orEmpty(default: Long = emptyLong) = this ?: default

/**
 * Proceed and Return Non-Null Value of Nullable Long
 * @return [Long]
 */
val Long?.orEmpty get() = orEmpty(emptyLong)

/**
 * Proceed and Return Non-Null Value of Nullable Float
 * @param default is the replacement of nullable value of Float
 * @return [Float]
 */
fun Float?.orEmpty(default: Float = emptyFloat) = this ?: default

/**
 * Proceed and Return Non-Null Value of Nullable Float
 * @return [Float]
 */
val Float?.orEmpty get() = orEmpty(emptyFloat)

/**
 * Proceed and Return Non-Null Value of Nullable Int
 * @param default is the replacement of nullable value of Int
 * @return [Int]
 */
fun Int?.orEmpty(default: Int = emptyInt) = this ?: default

/**
 * Proceed and Return Non-Null Value of Nullable Int
 * @return [Int]
 */
val Int?.orEmpty get() = orEmpty()

/**
 * Proceed and Return Non-Null Value of Nullable Boolean
 * @param default is the replacement of nullable value of Boolean
 * @return [Boolean]
 */
fun Boolean?.orEmpty(default: Boolean = emptyBoolean) = this ?: default

/**
 * Proceed and Return Non-Null Value of Nullable Boolean
 * @return [Boolean]
 */
val Boolean?.orEmpty get() = orEmpty()

fun String?.isEmailPattern() = Patterns.EMAIL_ADDRESS.matcher(orEmpty).matches()
