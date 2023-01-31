package com.stellkey.android.view.carer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentKidProfileBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.emptyBoolean
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.RewardModel
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.model.request.CustomRewardAssignKidRequest
import com.stellkey.android.model.request.DeleteChildTaskRequest
import com.stellkey.android.model.request.GlobalRewardAssignKidRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.util.SwipeToDeleteTaskCallback
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.account.EditProfileFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.profile.adapter.ActiveRewardAdapter
import com.stellkey.android.view.carer.profile.adapter.ActiveTaskAdapter
import com.stellkey.android.view.carer.reward.AddRewardFragment
import com.stellkey.android.view.carer.task.AddTaskFragment
import org.koin.android.ext.android.inject

class KidProfileFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentKidProfileBinding
    private val viewModel by inject<ProfileViewModel>()

    private lateinit var activeTaskAdapter: ActiveTaskAdapter
    private lateinit var activeRewardAdapter: ActiveRewardAdapter

    private var selectedPosition = emptyInt
    private var isRewardTabActive = emptyBoolean

    private var selectedKidId = emptyInt
    private var selectedKidAge = emptyInt

    companion object {

        @JvmStatic
        fun newInstance() = KidProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_kid_profile,
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

            deleteAssignmentSuccess.observe(viewLifecycleOwner) {
                activeTaskAdapter.removeAt(selectedPosition)
                selectedPosition = emptyInt

                viewModel.getCurrentCycleAssignment(profileId = AppPreference.getTempChildId())
            }

            unAssignCustomRewardForKidSuccess.observe(viewLifecycleOwner) {
                viewModel.getListReward(
                    profileId = AppPreference.getTempChildId(),
                    starCost = null
                )
            }

            unAssignGlobalRewardForKidSuccess.observe(viewLifecycleOwner) {
                viewModel.getListReward(
                    profileId = AppPreference.getTempChildId(),
                    starCost = null
                )
            }

            detailKid.observe(viewLifecycleOwner) {
                it?.let { it1 ->
                    setKidDetailData(it1)
                    setTaskData(it1.activeTasks.assignments)
                }
            }

            activeTasks.observe(viewLifecycleOwner) {
                it?.let { taskResponse -> setTaskData(taskResponse.assignments) }
            }

            getListRewardsSuccess.observe(viewLifecycleOwner) { rewards ->
                rewards?.let {
                    setRewardData(it)
                }
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()
    }

    override fun onResume() {
        super.onResume()

        AppPreference.getTempChildId().let {
            if (it == emptyInt) {
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                viewModel.getDetailKid(profileId = it)
            }
        }
    }

    private fun setKidDetailData(kidData: AllKidsModel) {
        selectedKidId = kidData.id
        selectedKidAge = kidData.age

        dataBinding.apply {
            ivAvatar.loadImage(
                kidData.profileIcon.icon,
                ImageCornerOptions.ROUNDED,
                100
            )
            tvProfileName.textOrNull = kidData.name
            tvKidAge.textOrNull = requireContext().resources.getString(
                R.string.kid_age,
                kidData.age
            )
            tvTotalStars.text = kidData.level.starsTotal.toString()
            tvProfileLevel.textOrNull = requireContext().resources.getString(
                R.string.kid_level,
                kidData.level.level
            )
            tvRewardsAvailable.text = kidData.tasksCompleted.toString()

            btnEditProfile.setOnClickListener {
                addFragment(EditProfileFragment.newInstance())

                AppPreference.putProfileType(Constant.FamilyType.KIDS)
                AppPreference.putTempChildId(selectedKidId)
                AppPreference.putTempChildProfilePIN(kidData.pin)
                AppPreference.putTempChildAge(selectedKidAge)
                AppPreference.putProfileIconId(kidData.iconId)
            }
        }
    }

    private fun setTaskData(taskData: ArrayList<AssignmentsModel>) {
        dataBinding.apply {
            if (taskData.isEmpty()) {
                AppPreference.putActiveCycle(false)

                tvLabelSwipeTask.isVisible = false
                rvProfileTask.isVisible = false

                cvAddTask.apply {
                    isVisible = true
                    setOnClickListener(onClickCallback)
                }
            } else {
                rvProfileTask.isVisible = true
                tvLabelSwipeTask.isVisible = true
                AppPreference.putActiveCycle(true)
                AppPreference.putTempChallengeStartDate(taskData[0].assignDate)

                val groupedActiveTask = mutableListOf<Pair<AssignmentsModel, List<TaskStarModel>>>()

                // for tasks
                taskData.groupBy { it.globalChallengeId }.forEach {
                    if (it.key != null) {
                        val listStarTask = it.value.map { assignment ->
                            TaskStarModel(isCompleted = assignment.completedAt != null)
                        }
                        groupedActiveTask.add(
                            Pair(
                                it.value.first(),
                                listStarTask
                            )
                        )
                    }
                }

                // for custom tasks
                taskData.groupBy { it.challengeId }.forEach {
                    if (it.key != null) {
                        val listStarTask = it.value.map { assignment ->
                            TaskStarModel(isCompleted = assignment.completedAt != null)
                        }
                        groupedActiveTask.add(
                            Pair(
                                it.value.first(),
                                listStarTask
                            )
                        )
                    }
                }

                if (groupedActiveTask.size < 3) {
                    cvAddTask.apply {
                        isVisible = true
                        setOnClickListener(onClickCallback)
                    }
                } else {
                    cvAddTask.apply {
                        isVisible = false
                        setOnClickListener(null)
                    }
                }

                activeTaskAdapter = ActiveTaskAdapter(
                    requireContext(),
                    groupedActiveTask.toArrayList(),
                )
                rvProfileTask.apply {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = activeTaskAdapter
                }
                val swipeHandler = object : SwipeToDeleteTaskCallback(requireContext()) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        selectedPosition = viewHolder.absoluteAdapterPosition
                        val selectedTask = activeTaskAdapter.selectTask(selectedPosition)
                        viewModel.deleteAssignment(
                            DeleteChildTaskRequest(
                                cycle = selectedTask.cycle,
                                kidId = AppPreference.getTempChildId(),
                                challengeId = selectedTask.challengeId,
                                globalChallengeId = selectedTask.globalChallengeId
                            )
                        )
                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(rvProfileTask)
            }
        }
    }

    private fun setRewardData(listRewards: List<RewardModel>) {
        dataBinding.apply {
            if (listRewards.isEmpty()) {
                tvLabelSwipeRewards.isVisible = false
                rvProfileReward.isVisible = false

                cvAddReward.apply {
                    isVisible = true
                    setOnClickListener(onClickCallback)
                }
            } else {
                rvProfileReward.isVisible = true
                tvLabelSwipeRewards.isVisible = true

                if (listRewards.size < 2) {
                    cvAddReward.apply {
                        isVisible = true
                        setOnClickListener(onClickCallback)
                    }
                } else {
                    cvAddReward.apply {
                        isVisible = false
                        setOnClickListener(null)
                    }
                }

                activeRewardAdapter = ActiveRewardAdapter(
                    listRewards.toArrayList()
                )
                rvProfileReward.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    adapter = activeRewardAdapter
                }

                val swipeHandler = object : SwipeToDeleteTaskCallback(requireContext()) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        selectedPosition = viewHolder.absoluteAdapterPosition
                        val selectedReward =
                            activeRewardAdapter.selectReward(viewHolder.absoluteAdapterPosition)

                        if (selectedReward.isGlobal) {
                            viewModel.postUnAssignGlobalRewardForKids(
                                request = GlobalRewardAssignKidRequest(
                                    kidId = listOf(AppPreference.getTempChildId()),
                                    globalRewardId = selectedReward.id
                                )
                            )
                        } else {
                            viewModel.postUnAssignCustomRewardForKids(
                                request = CustomRewardAssignKidRequest(
                                    kidId = listOf(AppPreference.getTempChildId()),
                                    rewardId = selectedReward.id
                                )
                            )
                        }

                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(rvProfileReward)
            }
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            AppPreference.deleteTempCreateMemberData()
            AppPreference.deleteAddTaskData()

            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            cvTasks.setOnClickListener(onClickCallback)
            cvRewards.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                AppPreference.deleteTempCreateMemberData()
                AppPreference.deleteAddTaskData()

                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.cvTasks -> {
                dataBinding.apply {
                    cvTasks.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    tvTasks.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    cvRewards.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                    tvRewards.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )

                    isRewardTabActive = false
                    clProfileTask.isVisible = true
                    clProfileReward.isVisible = false
                    viewModel.getCurrentCycleAssignment(profileId = AppPreference.getTempChildId())
                }
            }

            dataBinding.cvRewards -> {
                dataBinding.apply {
                    cvTasks.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                    tvTasks.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    cvRewards.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    tvRewards.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                    isRewardTabActive = true
                    clProfileTask.isVisible = false
                    clProfileReward.isVisible = true
                    viewModel.getListReward(
                        profileId = AppPreference.getTempChildId(),
                        starCost = null
                    )
                }
            }

            dataBinding.cvAddTask -> {
                AppPreference.putTempChildId(selectedKidId)
                addFragment(AddTaskFragment.newInstance())
                AddTaskFragment.kidAge = selectedKidAge
            }

            dataBinding.cvAddReward -> {
                AppPreference.putTempChildId(selectedKidId)
                addFragment(AddRewardFragment.newInstance())
            }
        }
    }

}