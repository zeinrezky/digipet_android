package com.stellkey.android.view.intro.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentLoginPINBinding
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.model.request.CarerLoginRequest
import com.stellkey.android.model.request.KidLoginRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.child.ChildMainAct
import kotlinx.android.synthetic.main.fragment_login_p_i_n.*
import org.koin.android.ext.android.inject
import java.util.*

class LoginPINFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentLoginPINBinding

    //private val binding by viewBinding<FragmentLoginPINBinding>()
    private val viewModel by inject<LoginViewModel>()

    private var tempPIN = emptyString

    companion object {
        @JvmStatic
        fun newInstance() = LoginPINFragment()
        var profileType = emptyString
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login_p_i_n,
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
            carerLoginSuccess.observe(viewLifecycleOwner) {
                AppPreference.putKidLogin(false)
                AppPreference.putCompleteLogin(true)
                AppPreference.putLoginToken(AppPreference.getMainCarerLoginToken())

                startActivity(Intent(requireContext(), HomeAct::class.java))
                requireActivity().finish()
            }
            loginResponse.observe(viewLifecycleOwner) {
                AppPreference.putKidLogin(true)
                AppPreference.putKidToken(it.token)
                AppPreference.putCompleteLogin(true)
                AppPreference.putLoginToken(AppPreference.getMainCarerLoginToken())

                startActivity(Intent(requireContext(), ChildMainAct::class.java))
                requireActivity().finish()
            }
            loginFailed.observe(viewLifecycleOwner) {
                clErrorMessage.visibility = View.VISIBLE
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {}

    private fun validateForm() {
        clErrorMessage.visibility = View.INVISIBLE
        val tz: TimeZone = TimeZone.getDefault()
        val now = Date()
        val tempTimezone = tz.getOffset(now.time) / 3600000

        when (profileType) {
            Constant.ProfileIconType.PROFILE_ICON_CARER -> {
                viewModel.postCarerLogin(
                    CarerLoginRequest(
                        loginToken = AppPreference.getLoginToken(),
                        pin = tempPIN,
                        deviceToken = "updatedFromLogin",
                        timezone = tempTimezone,
                        locale = "en"
                    )
                )
            }
            Constant.ProfileIconType.PROFILE_ICON_KID -> {
                viewModel.postKidLogin(
                    KidLoginRequest(
                        loginToken = AppPreference.getLoginToken(),
                        pin = tempPIN,
                        deviceToken = "updatedFromLogin",
                        settingTimezone = tempTimezone,
                        settingLocale = "en"
                    )
                )
            }
        }

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
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("1")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("1")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("1")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }

            }
            dataBinding.tvInputDigit2 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("2")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("2")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("2")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("2")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit3 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("3")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("3")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("3")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("3")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit4 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("4")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("4")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("4")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("4")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit5 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("5")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("5")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("5")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("5")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit6 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("6")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("6")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("6")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("6")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit7 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("7")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("7")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("7")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("7")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit8 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("8")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("8")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("8")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("8")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit9 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("9")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("9")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("9")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("9")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit0 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("0")
                        tempPIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("0")
                        tempPIN = tempPIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("0")
                        tempPIN = tempPIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("0")
                        tempPIN = tempPIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.ivDelete -> {
                dataBinding.apply {
                    if (etFourthDigit.text.toString().isNotEmpty()) {
                        etFourthDigit.text.clear()
                        tempPIN = StringBuilder(tempPIN).deleteAt(3).toString()
                    } else if (etThirdDigit.text.toString().isNotEmpty()) {
                        etThirdDigit.text.clear()
                        tempPIN = StringBuilder(tempPIN).deleteAt(2).toString()
                    } else if (etSecondDigit.text.toString().isNotEmpty()) {
                        tempPIN = StringBuilder(tempPIN).deleteAt(1).toString()
                        etSecondDigit.text.clear()
                    } else if (etFirstDigit.text.toString().isNotEmpty()) {
                        tempPIN = StringBuilder(tempPIN).deleteAt(0).toString()
                        etFirstDigit.text.clear()
                    }
                }
            }
        }
    }

}