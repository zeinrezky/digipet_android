package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentSettingBinding
import com.stellkey.android.model.request.UpdateLocaleRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.zeugmasolutions.localehelper.Locales
import org.koin.android.ext.android.inject


class SettingFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentSettingBinding

    //private val binding by viewBinding<FragmentSettingBinding>()
    private val viewModel by inject<AccountViewModel>()

    private var selectedLocale = AppPreference.getCarerLocale()

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_setting,
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
                (activity as HomeAct).updateLocale(if (selectedLocale == "fr") Locales.French else Locales.English)
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {
        onBackPressed()
        setLanguageButton(selectedLocale)
    }

    private fun setLanguageButton(language: String) {
        dataBinding.apply {
            if (language == "en") {
                cvLanguageSwitch.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue
                    )
                )
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
                cvLanguageSwitch.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
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
            cvSoundOff.setOnClickListener(onClickCallback)
            cvSoundOn.setOnClickListener(onClickCallback)
            cvNotificationOff.setOnClickListener(onClickCallback)
            cvNotificationOn.setOnClickListener(onClickCallback)
            cvFrench.setOnClickListener(onClickCallback)
            cvEnglish.setOnClickListener(onClickCallback)
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            (activity as HomeAct).showMenu(isShow = true)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                (activity as HomeAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.cvSoundOff -> {
                dataBinding.apply {
                    cvSoundSwitch.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvSoundOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    cvSoundOn.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvSoundOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            dataBinding.cvSoundOn -> {
                dataBinding.apply {
                    cvSoundSwitch.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    tvSoundOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    cvSoundOn.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvSoundOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }
            }
            dataBinding.cvNotificationOff -> {
                dataBinding.apply {
                    cvNotificationSwitch.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    tvNotificationOff.setTextColor(
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
                    tvNotificationOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            dataBinding.cvNotificationOn -> {
                dataBinding.apply {
                    cvNotificationSwitch.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    tvNotificationOff.setTextColor(
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
                    tvNotificationOn.setTextColor(
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
                AppPreference.getCarerLocale()
                viewModel.putCarerLocale(UpdateLocaleRequest(locale = selectedLocale))
            }
            dataBinding.cvEnglish -> {
                selectedLocale = "en"
                setLanguageButton(selectedLocale)
                viewModel.putCarerLocale(UpdateLocaleRequest(locale = selectedLocale))
            }
        }
    }

}