package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentEditPasswordBinding
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.model.request.EditPasswordRequest
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import org.koin.android.ext.android.inject

class EditPasswordFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentEditPasswordBinding

    //private val binding by viewBinding<FragmentEditPasswordBinding>()
    private val viewModel by inject<AccountViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = EditPasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_edit_password,
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

            updateMainCarerPasswordSuccess.observe(viewLifecycleOwner) {
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {
        dataBinding.apply {
            validateForm()
            etOldPassword.afterTextChanged {
                validateForm()
            }
            etNewPassword.afterTextChanged {
                validateForm()
            }
            etConfirmNewPassword.afterTextChanged {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        dataBinding.apply {
            if (!etOldPassword.text.isNullOrEmpty() && !etNewPassword.text.isNullOrEmpty() && !etConfirmNewPassword.text.isNullOrEmpty()) {
                if (etNewPassword.text.toString() == etConfirmNewPassword.text.toString()) {
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
                    clErrorMessage.visibility = View.INVISIBLE
                }
                else {
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
                    clErrorMessage.visibility = View.VISIBLE
                }
            } else {
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
                clErrorMessage.visibility = View.INVISIBLE
            }
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.btnSave -> {
                dataBinding.apply {
                    viewModel.putUpdateMainCarerPassword(
                        EditPasswordRequest(
                            oldPassword = etOldPassword.text.toString(),
                            password = etNewPassword.text.toString(),
                            confirmPassword = etConfirmNewPassword.text.toString()
                        )
                    )
                }
            }
        }
    }

}