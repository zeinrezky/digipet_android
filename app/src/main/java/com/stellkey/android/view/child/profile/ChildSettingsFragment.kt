package com.stellkey.android.view.child.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentChildSettingsBinding
import com.stellkey.android.model.request.UpdateLocaleRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.zeugmasolutions.localehelper.Locales
import org.koin.android.ext.android.inject

class ChildSettingsFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentChildSettingsBinding

    private val viewModel by inject<ChildViewModel>()

    private var selectedLocale = AppPreference.getKidLocale()

    companion object {
        @JvmStatic
        fun newInstance() = ChildSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_child_settings,
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

            kidLocaleResponse.observe(viewLifecycleOwner) { it ->
                AppPreference.putKidLocale(it.locale)
                AppPreference.putUpdateLocale(true)

                selectedLocale = AppPreference.getKidLocale()
                (activity as ChildMainAct).updateLocale(if (selectedLocale == "fr") Locales.French else Locales.English)
            }

        }

        (activity as ChildMainAct).showMenu(isShow = false)
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
                ivFr.isVisible = false
                tvFr.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue
                    )
                )
                ivEn.isVisible = true
                tvEn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            } else if (language == "fr") {
                ivFr.isVisible = true
                tvFr.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                ivEn.isVisible = false
                tvEn.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_blue
                    )
                )
            }
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            tvAlertsOff.setOnClickListener(onClickCallback)
            tvAlertsOn.setOnClickListener(onClickCallback)
            tvSoundOff.setOnClickListener(onClickCallback)
            tvSoundOn.setOnClickListener(onClickCallback)
            tvMusicOff.setOnClickListener(onClickCallback)
            tvMusicOn.setOnClickListener(onClickCallback)
            tvEn.setOnClickListener(onClickCallback)
            tvFr.setOnClickListener(onClickCallback)
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            (activity as ChildMainAct).showMenu(isShow = false)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                (activity as ChildMainAct).showMenu(isShow = false)
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.tvAlertsOff -> {
                dataBinding.apply {
                    ivAlertsOff.isVisible = true
                    tvAlertsOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    ivAlertsOn.isVisible = false
                    tvAlertsOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                }
            }
            dataBinding.tvAlertsOn -> {
                dataBinding.apply {
                    ivAlertsOff.isVisible = false
                    tvAlertsOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    ivAlertsOn.isVisible = true
                    tvAlertsOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            dataBinding.tvSoundOff -> {
                dataBinding.apply {
                    ivSoundOff.isVisible = true
                    tvSoundOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    ivSoundOn.isVisible = false
                    tvSoundOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                }
            }
            dataBinding.tvSoundOn -> {
                dataBinding.apply {
                    ivSoundOff.isVisible = false
                    tvSoundOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    ivSoundOn.isVisible = true
                    tvSoundOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            dataBinding.tvMusicOff -> {
                dataBinding.apply {
                    ivMusicOff.isVisible = true
                    tvMusicOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    ivMusicOn.isVisible = false
                    tvMusicOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                }
            }
            dataBinding.tvMusicOn -> {
                dataBinding.apply {
                    ivMusicOff.isVisible = false
                    tvMusicOff.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    ivMusicOn.isVisible = true
                    tvMusicOn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
            dataBinding.tvEn -> {
                selectedLocale = "en"
                setLanguageButton(selectedLocale)
                viewModel.putKidLocale(UpdateLocaleRequest(locale = selectedLocale))
            }
            dataBinding.tvFr -> {
                selectedLocale = "fr"
                setLanguageButton(selectedLocale)
                viewModel.putKidLocale(UpdateLocaleRequest(locale = selectedLocale))
            }
        }
    }
}