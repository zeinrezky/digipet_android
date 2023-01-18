package com.stellkey.android.helper.extension

import android.text.format.DateUtils
import androidx.fragment.app.FragmentManager
import com.google.android.material.R
import com.google.android.material.datepicker.*
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun FragmentManager.timePicker(
    selection: Long? = null,
    title: String? = null,
    fullScreen: Boolean = false,
    constraint: Pair<Long?, Long?>? = null,
    onSaveClickAction: ((Long) -> Unit)?
): MaterialDatePicker<Long> {

    val constraintBuilderRange = CalendarConstraints.Builder()

    constraintBuilderRange.setStart(constraint?.first.orEmpty)
    constraintBuilderRange.setEnd(constraint?.second.orEmpty)

    val minValidator = DateValidatorPointForward.from(constraint?.first.orEmpty)
    val maxValidator = DateValidatorPointBackward.before(constraint?.second.orEmpty)

    val validators = listOf(minValidator, maxValidator)
    val dateValidator = CompositeDateValidator.allOf(validators)

    constraintBuilderRange.setValidator(dateValidator)

    return MaterialDatePicker.Builder.datePicker().apply {
        if (selection != null) setSelection(selection)
        if (constraint != null) setCalendarConstraints(constraintBuilderRange.build())

        if (constraint != null) setCalendarConstraints(constraintBuilderRange.build())

        setTheme(
            if (!fullScreen) R.style.ThemeOverlay_MaterialComponents_MaterialCalendar
            else R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen
        )

        if (!title.isNullOrEmpty()) {
            setTitleText(title)
        }

    }.build().apply {
        addOnPositiveButtonClickListener {
            if (onSaveClickAction != null) {
                onSaveClickAction(it)
            } else dismiss()
        }
    }.also {
        it.show(this, javaClass.simpleName)
    }
}

fun FragmentManager.rangeTimePicker(
    selectedRange: Pair<Long, Long>? = null,
    title: String? = null,
    constraint: Pair<Date?, Date?>? = null,
    fullScreen: Boolean = false,
    onSaveClickAction: ((Pair<Long, Long>) -> Unit)?
): MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {
    val constraintBuilder = CalendarConstraints.Builder()
    val minValidator = DateValidatorPointForward.from(constraint?.first?.time.orEmpty)
    val maxValidator = DateValidatorPointBackward.before(constraint?.second?.time.orEmpty)

    val validators = listOf(minValidator, maxValidator)
    val dateValidator = CompositeDateValidator.allOf(validators)
    constraintBuilder.setValidator(dateValidator)

    return MaterialDatePicker.Builder.dateRangePicker().apply {
        if (!title.isNullOrEmpty()) {
            setTitleText(title)
        }

        if (constraint != null) setCalendarConstraints(constraintBuilder.build())

        setTheme(
            if (!fullScreen) R.style.ThemeOverlay_MaterialComponents_MaterialCalendar
            else R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen
        )

        if (selectedRange != null) {
            setSelection(androidx.core.util.Pair(selectedRange.first, selectedRange.second))
        }

    }.build().apply {
        addOnPositiveButtonClickListener {
            if (onSaveClickAction != null) {
                onSaveClickAction(Pair(it.first.orEmpty(), it.second.orEmpty()))
            } else dismiss()
        }
    }.also {
        it.show(this, javaClass.simpleName)
    }
}


fun getTimeZoneById(id: String = "GMT+07:00"): TimeZone {
    return TimeZone.getTimeZone(id)
}

val applicationTimeZone get() = getTimeZoneById()

fun dateFormatter(format: String = "yyyy-MM-dd"): SimpleDateFormat {
    return SimpleDateFormat(format, Locale.getDefault()).apply {
        timeZone = applicationTimeZone
    }
}

fun getCurrentDate(): String = dateFormatter("yyyy-MM-dd HH:mm:ss").format(Date())
fun getCurrentDayMonth(format: String = "M"): String = dateFormatter(format).format(Date())
fun getCurrentDayYear(): String = dateFormatter("yyyy").format(Date())

val defaultDateFormat get() = dateFormatter()

val String.applicationDateFormatter: String
    get() =
        dateFormatter("dd-MM-yyyy HH:mm:ss").parse(this).let {
            dateFormatter("dd MMM yyyy").format(it ?: Date())
        }

fun String.formatDate(
    from: String = "dd-MM-yyyy HH:mm:ss",
    to: String = "dd MMM yyyy"
): String = dateFormatter(from).parse(this).let {
    dateFormatter(to).format(it ?: Date())
}

fun String.isDateToday(format: String): Boolean {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = sdf.parse(this)
    return DateUtils.isToday(date.time)
}

const val SECOND_MILLIS = 1000
const val MINUTE_MILLIS = 60 * SECOND_MILLIS
const val HOUR_MILLIS = 60 * MINUTE_MILLIS
const val DAY_MILLIS = 24 * HOUR_MILLIS
const val WEEK_MILLIS = 7 * DAY_MILLIS

fun String.getTimeAgo(originFormat: String): String {
    val originDate = dateFormatter(originFormat).parse(this)
    var time = originDate.time
    if (time < 1000000000000L) {
        time *= 1000
    }

    val diff = Date().time - time
    Timber.e("diff $diff ")
    return when {
        diff < MINUTE_MILLIS -> "a few moments ago"
        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
        diff < 7 * DAY_MILLIS -> "${diff / DAY_MILLIS} days ago"
        diff / DAY_MILLIS < 30 -> "${diff / WEEK_MILLIS} weeks ago"
        diff / DAY_MILLIS < 365 -> "${(diff / DAY_MILLIS) / 30} months ago"
        diff / DAY_MILLIS > 365 -> "${((diff / WEEK_MILLIS) / 4) / 12} years ago"
        else -> "${diff / DAY_MILLIS} days ago (error)"
    }
}


fun getYesterday(): Date {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    return cal.time
}

fun String.isDateYesterday(originFormat: String): Boolean {
    val originDate = dateFormatter(originFormat).parse(this)
    val s2 = dateFormatter(originFormat).format(getYesterday())

    return originDate == dateFormatter(originFormat).parse(s2)
}


fun stringDateToCalendar(dateString: String?, formatString: String): Calendar? {

    if (dateString == null || dateString.isEmpty() || formatString.isBlank())
        return null

    val inputDateFormat = SimpleDateFormat(formatString, Locale.getDefault())

    return try {
        inputDateFormat.parse(dateString)?.let {
            val cal = Calendar.getInstance()
            cal.time = it
            cal
        }
    } catch (e: ParseException) {
        null
    }

}