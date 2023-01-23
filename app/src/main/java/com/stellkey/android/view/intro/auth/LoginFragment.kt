package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentLoginBinding
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.helper.extension.isEmailPattern
import com.stellkey.android.model.request.LoginRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject
import java.util.*

class LoginFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentLoginBinding

    private val viewModel by inject<LoginViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
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

            snackbarMessage.observe(viewLifecycleOwner) {
                showToast(it.toString())
            }
            loginResponse.observe(viewLifecycleOwner) {
                AppPreference.putMainCarerLoginToken(it.loginToken)
                AppPreference.putLoginToken(it.token)
                addFragment(LoginChooseProfileFragment.newInstance())
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        validateForm()

        dataBinding.apply {
            etEmail.afterTextChanged {
                validateForm()
            }
            etPassword.afterTextChanged {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        dataBinding.apply {
            if (!etEmail.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()) {
                if (etEmail.text.toString().isEmailPattern())
                    btnLogin.apply {
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
                    btnLogin.apply {
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
                btnLogin.apply {
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
            cvQrCodeLogin.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.btnLogin -> {
                dataBinding.apply {
                    val tz: TimeZone = TimeZone.getDefault()
                    val now = Date()
                    val tempTimezone = tz.getOffset(now.time) / 3600000

                    viewModel.postLogin(
                        LoginRequest(
                            email = etEmail.text.toString(),
                            password = etPassword.text.toString(),
                            locale = "en",
                            timezone = tempTimezone,
                            deviceToken = "updatedFromLogin"
                        )
                    )
                }
            }

            dataBinding.cvQrCodeLogin -> {
                addFragment(QRLoginScannerFragment.newInstance())
            }
        }
    }

}