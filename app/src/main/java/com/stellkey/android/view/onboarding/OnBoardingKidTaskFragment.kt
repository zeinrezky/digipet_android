package com.stellkey.android.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentOnboardingKidTaskBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment

class OnBoardingKidTaskFragment : BaseFragment() {

    private val binding by viewBinding<FragmentOnboardingKidTaskBinding>()

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingKidTaskFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_kid_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {
        binding.apply {
            ivTodayTask.loadImage(
                Constant.OnBoarding.TASK_PRACTICE_INSTRUMENT,
                ImageCornerOptions.ROUNDED,
                100
            )
            ivSecondTodayTask.loadImage(
                Constant.OnBoarding.TASK_PUT_DIRTY_CLOTHES,
                ImageCornerOptions.ROUNDED,
                100
            )
            ivThirdTodayTask.loadImage(
                Constant.OnBoarding.TASK_MAKE_BED,
                ImageCornerOptions.ROUNDED,
                100
            )
        }
    }

    private fun setOnClick() {
        binding.apply {
            ivOnBoardingBackground.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivOnBoardingBackground -> {
                addFragment(OnBoardingKidPetFragment.newInstance())
            }
        }
    }
}