package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentRegisterChildBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment

class RegisterChildFragment : BaseFragment() {

    private val binding by viewBinding<FragmentRegisterChildBinding>()

    companion object {
        @JvmStatic
        fun newInstance() = RegisterChildFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_child, container, false)
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
            etChildName.afterTextChanged {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        binding.apply {
            if (!etChildName.text.isNullOrEmpty()) {
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

    private fun setOnClick() {
        binding.apply {
            ivBack.setOnClickListener(onClickCallback)
            ivEdit.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            binding.ivEdit -> {
                AllProfileIconsFragment.profileIconType = Constant.ProfileIconType.PROFILE_ICON_KID
                addFragment(AllProfileIconsFragment.newInstance())
            }
            binding.btnNext -> {
                AppPreference.putTempChildName(binding.etChildName.text.toString())
                addFragment(RegisterProfilePINFragment.newInstance())
            }
        }
    }
}