package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentRegisterSetupProfileBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.request.DefaultCarerRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject

class RegisterSetupProfileFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentRegisterSetupProfileBinding

    //private val binding by viewBinding<FragmentRegisterSetupProfileBinding>()
    private val viewModel by inject<RegisterViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = RegisterSetupProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_setup_profile,
                container,
                false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { bool ->
                bool.let { loading ->
                    if (loading) {
                        showWaitingDialog()
                    } else {
                        hideWaitingDialog()
                    }
                }
            }
            setupDefaultCarerSuccess.observe(viewLifecycleOwner) {

                AppPreference.putTempPassword(dataBinding.etPassword.text.toString())
                addFragment(RegisterChildFragment.newInstance())
            }

        }

        setView()
        setOnClick()
    }

    override fun onResume() {
        super.onResume()
        if (AppPreference.getProfileIconId() != emptyInt) {
            dataBinding.ivAvatar.loadImage(
                AppPreference.getProfileIcon(),
                ImageCornerOptions.ROUNDED,
                100
            )
            validateForm()
        }
    }

    private fun setView() {
        validateForm()

        dataBinding.apply {
            dataBinding.apply {
                etName.afterTextChanged {
                    validateForm()
                }

                etPassword.afterTextChanged {
                    validateForm()
                }

                etPin.afterTextChanged {
                    validateForm()
                }
            }
        }
    }

    private fun validateForm() {
        dataBinding.apply {
            if (AppPreference.getProfileIconId() != emptyInt && !etName.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty() && !etPin.text.isNullOrEmpty()) {
                btnSave.apply {
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
                btnSave.apply {
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
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            ivEdit.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.ivEdit -> {
                AllProfileIconsFragment.profileIconType =
                    Constant.ProfileIconType.PROFILE_ICON_CARER
                addFragment(AllProfileIconsFragment.newInstance())
            }
            dataBinding.btnSave -> {
                viewModel.postSetupDefaultCarer(
                    DefaultCarerRequest(
                        name = dataBinding.etName.text.toString(),
                        password = dataBinding.etPassword.text.toString(),
                        pin = dataBinding.etPin.text.toString(),
                        iconId = AppPreference.getProfileIconId()
                    )
                )
            }
        }
    }
}