package com.stellkey.android.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.ln
import kotlin.math.pow

class UtilityHelper {

    companion object {
        fun setImage(context: Context, url: String, img: ImageView) {
            if (url.isNotEmpty()) {
                Glide.with(context)
                    .load(url)
                    .placeholder(android.R.color.transparent)
                    .into(img)
            } else {
                img.setBackgroundResource(android.R.color.transparent)
            }
        }

        fun alertDialogNotCancelable(
            context: Context?,
            text: String,
            listener: DialogInterface.OnClickListener
        ) {
            context?.let {
                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setMessage(text)
                alertDialog.setCancelable(false)
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener)
                alertDialog.show()
            }
        }

        fun getTimezone(value: String): Int {
            val timezone = value.substring(5..5)
            return if (value.contains("-")) -timezone.toInt() else timezone.toInt()
        }

        fun rpFormatter(value: Int): String {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(value.toDouble())
                .replace("Rp", "Rp ")
                .replace(",00", "")
        }


        fun getSdfDayMonthYear(date: Date): String {
            return SimpleDateFormat("dd MMM yyyy", Locale.US).format(date)
        }

        fun getSdfHourMinute(date: Date): String {
            return SimpleDateFormat("hh:mm a", Locale.US).format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun getSdfDay(date: String): String {
            val stringToDate = SimpleDateFormat("dd MMM yyyy").parse(date)
            return if (stringToDate != null) SimpleDateFormat("d").format(stringToDate) else "0"
        }

        @SuppressLint("SimpleDateFormat")
        fun getSdfMonth(date: String): String {
            val stringToDate = SimpleDateFormat("dd MMM yyyy").parse(date)
            return if (stringToDate != null) SimpleDateFormat("M").format(stringToDate) else "0"
        }

        @SuppressLint("SimpleDateFormat")
        fun getSdfYear(date: String): String {
            val stringToDate = SimpleDateFormat("dd MMM yyyy").parse(date)
            return if (stringToDate != null) SimpleDateFormat("yyyy").format(stringToDate) else "0"
        }

        @SuppressLint("SimpleDateFormat")
        fun getSdfDMY(date: String): String {
            val stringToDate = SimpleDateFormat("d-M-yyyy").parse(date)
            return if (stringToDate != null) SimpleDateFormat("MMM dd, yyyy").format(stringToDate) else ""
        }

        fun checkAppInstalled(context: Context, packageName: String): Boolean {
            val packageManager = context.packageManager
            var applicationInfo: ApplicationInfo? = null
            try {
                applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return applicationInfo != null
        }

        fun hideSoftKeyboard(activity: Activity) {
            val mgr = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mgr.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }

        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val regex = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$"
            pattern = Pattern.compile(regex)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

        fun isValidEmail(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }

        fun getFormattedNumber(count: Long): String {
            if (count < 1000) return "" + count
            val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
            return String.format("%.1f%c", count / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
        }

        fun formatValue(value: Float): String {
            var value2 = value
            val arr = arrayOf("", "K", "M", "B", "T", "P", "E")
            var index = 0
            while (value2 / 1000 >= 1) {
                value2 = value2 / 1000
                index++
            }
            val decimalFormat = DecimalFormat("#.#")
            return String.format("%s %s", decimalFormat.format(value2), arr[index])
        }

        fun <T> List<T>.toArrayList(): ArrayList<T> {
            return ArrayList(this)
        }
    }

}