package com.stellkey.android.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentOnboardingTaskActionBinding
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.task.adapter.TaskStarAdapter

class OnBoardingTaskActionFragment : BaseFragment() {

    private val binding by viewBinding<FragmentOnboardingTaskActionBinding>()

    private lateinit var taskStarAdapter: TaskStarAdapter

    companion object {
        @JvmStatic
        fun newInstance() = OnBoardingTaskActionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_task_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {
        binding.apply {
            ivTodayChallenge.loadImage(
                Constant.OnBoarding.TASK_PUT_DIRTY_CLOTHES,
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
                addFragment(OnBoardingFinishFragment.newInstance())
            }
        }
    }

}