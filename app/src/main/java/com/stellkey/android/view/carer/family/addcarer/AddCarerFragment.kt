package com.stellkey.android.view.carer.family.addcarer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogCancelAddMemberBinding
import com.stellkey.android.databinding.FragmentAddCarerBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.carer.family.FamilyFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.intro.auth.AllProfileIconsFragment
import com.stellkey.android.view.base.BaseFragment

class AddCarerFragment : BaseFragment() {

    private val binding by viewBinding<FragmentAddCarerBinding>()

    private lateinit var dialogCancelBinding: DialogCancelAddMemberBinding
    private lateinit var cancelDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = AddCarerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_carer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    override fun onResume() {
        super.onResume()
        if (AppPreference.getProfileIconId() != emptyInt)
            binding.ivAvatar.loadImage(
                AppPreference.getProfileIcon(),
                ImageCornerOptions.ROUNDED,
                100
            )
    }

    private fun setView() {
        validateForm()

        binding.apply {
            etCarerName.afterTextChanged {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        binding.apply {
            if (!etCarerName.text.isNullOrEmpty()) {
                btnNext.apply {
                    isEnabled = true
                    isClickable = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    setOnClickListener(onClickCallback)
                }
            } else
                btnNext.apply {
                    isEnabled = false
                    isClickable = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primary_disabled
                        )
                    )
                    setOnClickListener(null)
                }
        }
    }

    private fun initCancelDialog() {
        dialogCancelBinding = DialogCancelAddMemberBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        cancelDialog = requireActivity().alertDialog(
            view = dialogCancelBinding.root,
            isCancelable = false,
            fullScreen = false
        )
        cancelDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogCancelBinding.tvDesc.textOrNull =
            requireContext().getString(R.string.dialog_add_member_cancel_desc_carer)

        dialogCancelBinding.btnCancel.setOnClickListener {
            AppPreference.deleteTempCreateMemberData()

            requireActivity().supportFragmentManager.fragments.let {
                for (fragment in requireActivity().supportFragmentManager.fragments) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(fragment)
                        .commit()
                }
                requireActivity().supportFragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
            cancelDialog.dismiss()
            (activity as HomeAct).showMenu(isShow = true)
            addFragment(FamilyFragment.newInstance())
        }

        dialogCancelBinding.btnContinue.setOnClickListener {
            cancelDialog.dismiss()
        }

        cancelDialog.show()
    }

    private fun setOnClick() {
        binding.apply {
            ivBack.setOnClickListener(onClickCallback)
            ivEdit.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            binding.ivEdit -> {
                AllProfileIconsFragment.profileIconType =
                    Constant.ProfileIconType.PROFILE_ICON_CARER
                addFragment(AllProfileIconsFragment.newInstance())
            }
            binding.btnNext -> {
                AppPreference.putTempCarerName(binding.etCarerName.text.toString())
                addFragment(AddCarerPINFragment.newInstance())
            }
            binding.tvCancel -> {
                initCancelDialog()
            }
        }
    }

}