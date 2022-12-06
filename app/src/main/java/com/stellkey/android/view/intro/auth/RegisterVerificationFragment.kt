package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogInfoBinding
import com.stellkey.android.databinding.FragmentRegisterVerificationBinding
import com.stellkey.android.helper.extension.color
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.request.RegisterConfirmRequest
import com.stellkey.android.model.request.ResendPINRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject
import java.util.*

class RegisterVerificationFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentRegisterVerificationBinding

    //private val binding by viewBinding<FragmentRegisterVerificationBinding>()
    private val viewModel by inject<RegisterViewModel>()

    private var tempCode = emptyString
    private var tempTimezone = emptyInt

    private lateinit var dialogInfoBinding: DialogInfoBinding

    companion object {
        @JvmStatic
        fun newInstance() = RegisterVerificationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_verification,
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
            resendCodeSuccess.observe(viewLifecycleOwner) {
                initInfoDialog(
                    "Verification has been resent.",
                    "Check your email for the 4-digit code."
                )
            }
            confirmAccountResponse.observe(viewLifecycleOwner) {
                AppPreference.putCarerToken(it.carerToken)
                AppPreference.putCarerLocale("en")
                AppPreference.putTempTimezone(tempTimezone)

                addFragment(RegisterSetupProfileFragment.newInstance())
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {}

    private fun validateForm() {
        val tz: TimeZone = TimeZone.getDefault()
        val now = Date()
        tempTimezone = tz.getOffset(now.time) / 3600000

        viewModel.confirmAccount(
            RegisterConfirmRequest(
                token = tempCode,
                accountId = AppPreference.getAccountId(),
                deviceToken = "tokenforpushnotif",
                timezone = tempTimezone,
                locale = "en"
            )
        )
    }

    private fun initInfoDialog(textTitle: String, textDesc: String) {
        dialogInfoBinding = DialogInfoBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        val customSnackBar =
            Snackbar.make(dataBinding.clVerificationMainContainer, "", Snackbar.LENGTH_LONG)
        val layout = customSnackBar.view as Snackbar.SnackbarLayout

        dialogInfoBinding.apply {
            tvTitle.textOrNull(textTitle)
            tvDesc.textOrNull(textDesc)
            clInfo.setOnClickListener {
                customSnackBar.dismiss()
            }
        }

        val view: View = customSnackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        layout.setPadding(0, 0, 0, 0)
        layout.setBackgroundColor(context.color(R.color.transparent))
        layout.elevation = 0F
        layout.addView(dialogInfoBinding.root, 0)
        customSnackBar.show()
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            tvInputDigit1.setOnClickListener(onClickCallback)
            tvInputDigit2.setOnClickListener(onClickCallback)
            tvInputDigit3.setOnClickListener(onClickCallback)
            tvInputDigit4.setOnClickListener(onClickCallback)
            tvInputDigit5.setOnClickListener(onClickCallback)
            tvInputDigit6.setOnClickListener(onClickCallback)
            tvInputDigit7.setOnClickListener(onClickCallback)
            tvInputDigit8.setOnClickListener(onClickCallback)
            tvInputDigit9.setOnClickListener(onClickCallback)
            tvInputDigit0.setOnClickListener(onClickCallback)
            ivDelete.setOnClickListener(onClickCallback)
            tvResendPIN.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.tvInputDigit1 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("1")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("1")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("1")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("1")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }

            }
            dataBinding.tvInputDigit2 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("2")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("2")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("2")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("2")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit3 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("3")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("3")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("3")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("3")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit4 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("4")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("4")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("4")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("4")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit5 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("5")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("5")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("5")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("5")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit6 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("6")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("6")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("6")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("6")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit7 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("7")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("7")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("7")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("7")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit8 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("8")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("8")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("8")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("8")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit9 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("9")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("9")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("9")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("9")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit0 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("0")
                        tempCode = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("0")
                        tempCode = tempCode.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("0")
                        tempCode = tempCode.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("0")
                        tempCode = tempCode.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.ivDelete -> {
                dataBinding.apply {
                    if (etFourthDigit.text.toString().isNotEmpty()) {
                        etFourthDigit.text.clear()
                        tempCode = StringBuilder(tempCode).deleteAt(3).toString()
                    } else if (etThirdDigit.text.toString().isNotEmpty()) {
                        etThirdDigit.text.clear()
                        tempCode = StringBuilder(tempCode).deleteAt(2).toString()
                    } else if (etSecondDigit.text.toString().isNotEmpty()) {
                        tempCode = StringBuilder(tempCode).deleteAt(1).toString()
                        etSecondDigit.text.clear()
                    } else if (etFirstDigit.text.toString().isNotEmpty()) {
                        tempCode = StringBuilder(tempCode).deleteAt(0).toString()
                        etFirstDigit.text.clear()
                    }
                }
            }
            dataBinding.tvResendPIN -> {
                val resendRequest = ResendPINRequest(accountId = AppPreference.getAccountId())
                viewModel.resendCode(resendRequest)
            }
        }
    }

}