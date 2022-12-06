package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentSettingBinding
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject

class SettingFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentSettingBinding

    //private val binding by viewBinding<FragmentSettingBinding>()
    private val viewModel by inject<AccountViewModel>()

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

        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {}

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
                dataBinding.apply {
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
            dataBinding.cvEnglish -> {
                dataBinding.apply {
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
                }
            }
        }
    }

}