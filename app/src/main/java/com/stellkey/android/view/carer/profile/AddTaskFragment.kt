package com.stellkey.android.view.carer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAddTaskBinding
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.model.CategorizedTaskModel
import com.stellkey.android.model.RecommendedTaskModel
import com.stellkey.android.model.TaskModel
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.profile.adapter.GroupedTaskAdapter
import com.stellkey.android.view.carer.profile.adapter.RecommendedTaskAdapter
import org.koin.android.ext.android.inject

class AddTaskFragment : BaseFragment(), RecommendedTaskAdapter.Listener,
                        GroupedTaskAdapter.Listener {

    private lateinit var dataBinding: FragmentAddTaskBinding

    //private val binding by viewBinding<FragmentAddTaskBinding>()
    private val viewModel by inject<ProfileViewModel>()

    private lateinit var recommendedTaskAdapter: RecommendedTaskAdapter
    private lateinit var groupedTaskAdapter: GroupedTaskAdapter

    companion object {

        @JvmStatic
        fun newInstance() = AddTaskFragment()
        var kidAge = emptyInt
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_task,
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

            groupedChallenges.observe(viewLifecycleOwner) {
                it?.let { taskResponse ->
                    setRecommendedTaskData(taskResponse.recommendedTasks)
                    setGroupedTaskData(taskResponse.categories)
                }
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()

        viewModel.getGroupedChallenges(age = kidAge)
    }

    private fun setRecommendedTaskData(taskData: ArrayList<RecommendedTaskModel>) {
        dataBinding.apply {
            recommendedTaskAdapter = RecommendedTaskAdapter(
                requireContext(),
                taskData,
                this@AddTaskFragment
            )
            rvRecommendedTask.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = recommendedTaskAdapter
            }
        }
    }

    private fun setGroupedTaskData(taskData: ArrayList<CategorizedTaskModel>) {
        dataBinding.apply {
            groupedTaskAdapter = GroupedTaskAdapter(
                requireContext(),
                taskData,
                this@AddTaskFragment
            )
            rvGroupedTask.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = groupedTaskAdapter
            }
        }
    }

    //Recommended Task
    override fun onItemClicked(data: RecommendedTaskModel) {
        AppPreference.putTempSelectedGlobalChallengeId(data.id)
        AppPreference.putTempSelectedCategoryId(data.challengeCatId)
        AppPreference.putTempSelectedChallengeName(data.title)
        AppPreference.putTempSelectedChallengeIcon(data.icon)
        AddTaskDetailFragment.isCustomTask = false
        AddTaskDetailFragment.isGlobalTask = true


        addFragment(AddTaskDetailFragment.newInstance())
    }

    //Grouped by Categories Task
    override fun onGroupedTaskItemClicked(data: TaskModel) {
        AppPreference.putTempSelectedGlobalChallengeId(data.id)
        AppPreference.putTempSelectedChallengeId(data.id)
        AppPreference.putTempSelectedCategoryId(data.challengeCatId)
        AppPreference.putTempSelectedChallengeName(data.title)
        AppPreference.putTempSelectedChallengeIcon(data.icon)
        AddTaskDetailFragment.isCustomTask = false
        AddTaskDetailFragment.isGlobalTask = data.isGlobal

        addFragment(AddTaskDetailFragment.newInstance())
    }

    override fun onAddCustomTaskClicked(data: CategorizedTaskModel) {
        AppPreference.putTempSelectedCategoryId(data.id)
        AppPreference.putTempSelectedChallengeIcon(data.icon)
        AddTaskDetailFragment.isCustomTask = true
        AddTaskDetailFragment.isGlobalTask = false

        addFragment(AddTaskDetailFragment.newInstance())
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


}