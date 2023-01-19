package com.stellkey.android.view.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.helper.extension.gravityTop

open class BaseFragment : Fragment() {

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
    }

    fun addFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .addToBackStack(null)
            .commit()
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_progress)
        dialog.setCancelable(false)
    }

    fun showWaitingDialog() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun hideWaitingDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showSnackbar(viewLayout: View, message: String) {
        Snackbar.make(viewLayout, message, Snackbar.LENGTH_LONG).apply {
            gravityTop()
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorFadePrimary))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

}