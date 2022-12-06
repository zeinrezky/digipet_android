package com.stellkey.android.view.onboarding

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentOnboardingAddTaskBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import eightbitlab.com.blurview.RenderScriptBlur

class OnBoardingAddTaskFragment : BaseFragment() {
    private val binding by viewBinding<FragmentOnboardingAddTaskBinding>()

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingAddTaskFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {
        binding.apply {
            val radius = 20f
            // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
            val rootView: ViewGroup =
                requireActivity().window.decorView.findViewById(android.R.id.content)

            // Optional:
            // Set drawable to draw in the beginning of each blurred frame.
            // Can be used in case your layout has a lot of transparent space and your content
            // gets a too low alpha value after blur is applied.
            val windowBackground: Drawable = requireActivity().window.decorView.background

            blurView.setupWith(rootView, RenderScriptBlur(requireActivity())) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius)

            ivChallenge.loadImage(
                Constant.OnBoarding.TASK_MAKE_BED,
                ImageCornerOptions.ROUNDED,
                100
            )
            ivChallengeSecond.loadImage(
                Constant.OnBoarding.TASK_PUT_DIRTY_CLOTHES,
                ImageCornerOptions.ROUNDED,
                100
            )
            ivChallengeThird.loadImage(
                Constant.OnBoarding.TASK_EAT_VEGETABLE,
                ImageCornerOptions.ROUNDED,
                100
            )
            ivGroupedChallenge.loadImage(
                Constant.OnBoarding.TASK_PUT_DIRTY_CLOTHES,
                ImageCornerOptions.ROUNDED,
                100
            )
            ivGroupedChallengeSecond.loadImage(
                Constant.OnBoarding.TASK_BRUSH_TEETH,
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
                addFragment(OnBoardingChooseTaskFragment.newInstance())
            }
        }
    }

}