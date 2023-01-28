package com.stellkey.android.view.child.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentTaskCompletedDialogBinding
import kotlinx.android.parcel.Parcelize

class TaskCompletedDialog(val onButtonClicked: () -> Unit = {}) : DialogFragment() {

    private lateinit var dataBinding: FragmentTaskCompletedDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_task_completed_dialog,
            container,
            false
        )
        // Inflate the layout for this fragment
        dialog?.let {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(TASK_COMPLETED_PARAMS) }?.apply {
            val basicKidData = if (Build.VERSION.SDK_INT >= 33) {
                getParcelable(TASK_COMPLETED_PARAMS, TaskCompletedParams::class.java)
            } else {
                getParcelable(TASK_COMPLETED_PARAMS)
            }
            basicKidData?.let { setView(it) }
        }

        setOnClick()
    }

    private fun setView(taskCompletedParams: TaskCompletedParams) {
        dataBinding.apply {
            tvSubtitle.text = taskCompletedParams.subtitleText
            tvTodayTaskStar.text = taskCompletedParams.starSum
            tvTodayTaskDiamond.text = taskCompletedParams.gemSum
        }
    }

    private fun setOnClick() {
        dataBinding.ivDoneBtn.setOnClickListener {
            onButtonClicked.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    companion object {
        const val TAG = "TaskCompletedDialog"
        const val TASK_COMPLETED_PARAMS = "TASK_COMPLETED_PARAMS"
    }

    @Parcelize
    data class TaskCompletedParams(
        val subtitleText: String = "",
        val starSum: String = "-",
        val gemSum: String = "-"
    ) : Parcelable
}