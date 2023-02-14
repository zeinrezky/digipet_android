package com.stellkey.android.helper.extension

import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version ContextExt, v 0.1 14/02/23 18.43 by Nicolas Manurung
 */
fun Context.openUrl(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
    )
}