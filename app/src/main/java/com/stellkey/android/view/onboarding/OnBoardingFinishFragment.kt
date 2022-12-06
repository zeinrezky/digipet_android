package com.stellkey.android.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentOnboardingFinishBinding
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct

class OnBoardingFinishFragment : BaseFragment() {

    private val binding by viewBinding<FragmentOnboardingFinishBinding>()

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingFinishFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {
        binding.apply {}
    }

    private fun setOnClick() {
        binding.apply {
            btnStartStellkey.setOnClickListener(onClickCallback)
            btnTryAgain.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnStartStellkey -> {
                AppPreference.putFirstTime(false)
                startActivity(Intent(requireContext(), HomeAct::class.java))
                requireActivity().finish()
            }
            binding.btnTryAgain -> {
                addFragment(OnBoardingHomeFragment.newInstance())
            }
        }
    }

}