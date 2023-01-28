package com.stellkey.android.view.child.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogKidInfoBinding
import com.stellkey.android.helper.extension.loadFromUrl
import com.stellkey.android.helper.extension.loadImage
import kotlinx.android.parcel.Parcelize


class BasicKidDialog(val onCloseClicked: () -> Unit = {}, val onButtonClicked: () -> Unit = {}) :
    DialogFragment() {

    private lateinit var dataBinding: DialogKidInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_kid_info, container, false)
        dialog?.let {
            it.window?.requestFeature(Window.FEATURE_NO_TITLE)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(BASIC_KID_DIALOG_PARAM) }?.apply {
            val basicKidData = if (Build.VERSION.SDK_INT >= 33) {
                getParcelable(BASIC_KID_DIALOG_PARAM, BasicKidDialogParam::class.java)
            } else {
                getParcelable(BASIC_KID_DIALOG_PARAM)
            }
            basicKidData?.let { setView(it) }
        }

        setOnClick()
    }

    override fun onStart() {
        super.onStart()
        dialog ?: return
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)

    }

    private fun setView(dialogParam: BasicKidDialogParam) {
        dataBinding.apply {
            tvTitle.text = dialogParam.title
            tvSubtitle.text = dialogParam.subtitle
            tvImageTitle.text = dialogParam.imageTitle
            if (dialogParam.isShowingImage) {
                if (dialogParam.isImageFromRes) {
                    ivDialog.loadImage(dialogParam.imageImageDialogRes)
                } else {
                    ivDialog.loadFromUrl(dialogParam.imageAttachmentDialogLink)
                }
            } else {
                ivDialog.isInvisible = true
            }

            if (dialogParam.buttonTitle.isEmpty()) {
                ivDoneBtn.isVisible = false
            } else {
                tvButton.text = dialogParam.buttonTitle
            }
        }
    }

    private fun setOnClick() {
        dataBinding.ivClose.setOnClickListener {
            onCloseClicked.invoke()
            dismiss()
        }

        dataBinding.ivDoneBtn.setOnClickListener {
            onButtonClicked.invoke()
            dismiss()
        }
    }

    companion object {
        const val TAG = "BasicKidDialog"

        const val BASIC_KID_DIALOG_PARAM = "BASIC_KID_DIALOG_PARAM"
    }

    @Parcelize
    data class BasicKidDialogParam(
        val title: String = "",
        val subtitle: String = "",
        val imageAttachmentDialogLink: String = "",
        @DrawableRes val imageImageDialogRes: Int = R.drawable.ic_reward,
        // isImageFromRes = if true so it will set imageImageDialogRes as image
        // else imageAttachmentDialogLink as image
        val isImageFromRes: Boolean = true,
        val isShowingImage: Boolean = false,
        val imageTitle: String = "",
        val buttonTitle: String = ""
    ) : Parcelable
}