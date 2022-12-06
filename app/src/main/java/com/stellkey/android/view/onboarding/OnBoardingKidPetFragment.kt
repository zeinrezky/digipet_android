package com.stellkey.android.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentOnboardingKidPetBinding
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.view.base.BaseFragment

class OnBoardingKidPetFragment : BaseFragment() {

    private val binding by viewBinding<FragmentOnboardingKidPetBinding>()

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingKidPetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_kid_pet, container, false)
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
            ivOnBoardingBackground.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivOnBoardingBackground -> {
                addFragment(OnBoardingCompletedTaskFragment.newInstance())
            }
        }
    }

}