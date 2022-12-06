package com.stellkey.android.view.intro.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentRegisterProfileSettingsBinding
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.model.request.LoginRequest
import com.stellkey.android.model.request.SubscriptionRequest
import com.stellkey.android.model.request.UpdateLocaleRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.intro.intro.IntroAct
import com.stellkey.android.view.onboarding.OnBoardingAct
import com.zeugmasolutions.localehelper.Locales
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegisterProfileSettingsFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentRegisterProfileSettingsBinding

    //private val binding by viewBinding<FragmentRegisterProfileSettingsBinding>()
    private val viewModel by inject<RegisterViewModel>()

    private var selectedLocale = AppPreference.getCarerLocale()

    companion object {
        @JvmStatic
        fun newInstance() = RegisterProfileSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_profile_settings,
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

            carerLocaleResponse.observe(viewLifecycleOwner) { it ->
                AppPreference.putCarerLocale(it.locale)
                AppPreference.putUpdateLocale(true)

                selectedLocale = AppPreference.getCarerLocale()
                (activity as IntroAct).updateLocale(if (selectedLocale == "fr") Locales.French else Locales.English)
                requireActivity().supportFragmentManager.popBackStack()
            }

            subscriptionSuccess.observe(viewLifecycleOwner) {
                viewModel.postRegisterLogin(
                    LoginRequest(
                        email = AppPreference.getTempEmail(),
                        password = AppPreference.getTempPassword(),
                        locale = selectedLocale,
                        timezone = AppPreference.getTempTimezone(),
                        deviceToken = "updatedFromLogin"
                    )
                )
            }

            registerLoginResponse.observe(viewLifecycleOwner) {
                AppPreference.deleteTempLoginData()
                AppPreference.putCarerToken(it.token)
                AppPreference.putCompleteLogin(true)
                AppPreference.putFirstTime(true)

                startActivity(Intent(requireContext(), OnBoardingAct::class.java))
                requireActivity().finish()
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        setLanguageButton(selectedLocale)
    }

    private fun setLanguageButton(language: String) {
        dataBinding.apply {
            if (language == "en") {
                cvFrench.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue
                    )
                )
                tvFrench.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary_50
                    )
                )
                cvEnglish.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                tvEnglish.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
            } else if (language == "fr") {
                cvFrench.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue
                    )
                )
                tvFrench.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                cvEnglish.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                tvEnglish.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary_50
                    )
                )
            }
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            cvNotificationOff.setOnClickListener(onClickCallback)
            cvNotificationOn.setOnClickListener(onClickCallback)
            cvFrench.setOnClickListener(onClickCallback)
            cvEnglish.setOnClickListener(onClickCallback)
            btnStart.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.cvNotificationOff -> {
                dataBinding.apply {
                    cvNotificationTab.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    cvNotificationOn.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            dataBinding.cvNotificationOn -> {
                dataBinding.apply {
                    cvNotificationTab.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    tvOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    cvNotificationOn.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }
            }
            dataBinding.cvFrench -> {
                selectedLocale = "fr"
                setLanguageButton(selectedLocale)
                viewModel.putCarerLocale(UpdateLocaleRequest(locale = selectedLocale))
            }
            dataBinding.cvEnglish -> {
                selectedLocale = "en"
                setLanguageButton(selectedLocale)
                viewModel.putCarerLocale(UpdateLocaleRequest(locale = selectedLocale))
            }
            dataBinding.btnStart -> {
                var currentDateFormatted = emptyString
                var trialEndDateFormatted = emptyString

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val currentDate = LocalDateTime.now()
                    val trialEndDate = LocalDateTime.now().plusDays(7)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                    currentDateFormatted = currentDate.format(formatter)
                    trialEndDateFormatted = trialEndDate.format(formatter)

                }

                viewModel.postSubscription(
                    SubscriptionRequest(
                        expiresAt = trialEndDateFormatted,
                        startAt = currentDateFormatted,
                        subscriptionId = Constant.SubscriptionType.TRIAL,
                        status = 0
                    )
                )
            }
        }
    }


}