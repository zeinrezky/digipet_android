package com.stellkey.android.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentOnboardingHomeBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.task.adapter.TaskStarAdapter

class OnBoardingHomeFragment : BaseFragment() {

    private val binding by viewBinding<FragmentOnboardingHomeBinding>()

    private lateinit var taskStarAdapter: TaskStarAdapter

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingHomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {
        binding.apply {
            ivAvatar.loadImage(Constant.OnBoarding.KID_ICON, ImageCornerOptions.ROUNDED, 100)
            ivTodayChallenge.loadImage(
                Constant.OnBoarding.TASK_PUT_DISHES,
                ImageCornerOptions.ROUNDED,
                24
            )

            val taskStarList = arrayListOf<TaskStarModel>()
            for (i in 0..6)
                taskStarList.add(TaskStarModel(isCompleted = false))

            taskStarAdapter = TaskStarAdapter(
                requireContext(),
                taskStarList
            )

            rvAssignmentStar.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = taskStarAdapter
            }
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
                addFragment(OnBoardingFamilyFragment.newInstance())
            }
        }
    }
}