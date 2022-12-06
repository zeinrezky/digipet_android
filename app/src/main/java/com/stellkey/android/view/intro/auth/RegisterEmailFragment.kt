package com.stellkey.android.view.intro.auth

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentRegisterEmailBinding
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.isEmailPattern
import com.stellkey.android.model.request.RegisterEmailRequest
import com.stellkey.android.model.request.SubscriptionRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegisterEmailFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentRegisterEmailBinding

    //private val binding by viewBinding<FragmentRegisterEmailBinding>()
    private val viewModel by inject<RegisterViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = RegisterEmailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register_email, container, false)
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
            registerSuccess.observe(viewLifecycleOwner) {
                AppPreference.putTempEmail(dataBinding.etEmail.text.toString())
                addFragment(RegisterVerificationFragment.newInstance())
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        validateForm()

        dataBinding.apply {
            cbTnc.setOnCheckedChangeListener { _, _ ->
                validateForm()
            }

            etEmail.afterTextChanged {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        dataBinding.apply {
            if (cbTnc.isChecked && !etEmail.text.isNullOrEmpty()) {
                if (etEmail.text.toString().isEmailPattern())
                    btnRegisterNow.apply {
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
                else
                    btnRegisterNow.apply {
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
            } else
                btnRegisterNow.apply {
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
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.btnRegisterNow -> {
                dataBinding.apply {
                    viewModel.register(
                        RegisterEmailRequest(etEmail.text.toString())
                    )
                }
            }
        }
    }

}