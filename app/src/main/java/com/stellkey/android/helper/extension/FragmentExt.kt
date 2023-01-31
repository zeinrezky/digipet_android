package com.stellkey.android.helper.extension

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment


/**
 * @author Nicolas Manurung (nicolas.manurung@dana.id)
 * @version FragmentExt, v 0.1 31/01/23 22.53 by Nicolas Manurung
 */

fun Fragment.onBackBlockPressed(){
    requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
        /* override back pressing */
        override fun handleOnBackPressed() { /* nothing happen */}
    })
}