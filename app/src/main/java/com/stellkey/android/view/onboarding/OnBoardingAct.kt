package com.stellkey.android.view.onboarding

import android.os.Bundle
import com.stellkey.android.R
import com.stellkey.android.databinding.ActivityOnboardingBinding
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.view.base.BaseAct

class OnBoardingAct : BaseAct() {
    private val binding by viewBinding<ActivityOnboardingBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setView()
        setOnClick()
    }

    private fun setView() {
        addFragment(OnBoardingHomeFragment.newInstance())
    }

    private fun setOnClick() {}
}