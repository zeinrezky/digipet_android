package com.stellkey.android.view.carer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.model.request.DeleteChildTaskRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.util.SwipeToDeleteTaskCallback
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.account.EditProfileFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.profile.adapter.ActiveTaskAdapter
import com.stellkey.android.view.carer.reward.AddRewardFragment
import com.stellkey.android.view.carer.task.AddTaskFragment
import org.koin.android.ext.android.inject

class KidProfileFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentKidProfileBinding

    //private val binding by viewBinding<FragmentKidProfileBinding>()
    private val viewModel by inject<ProfileViewModel>()

    private lateinit var activeTaskAdapter: ActiveTaskAdapter

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

            detailKid.observe(viewLifecycleOwner) {
                it?.let { it1 ->
                    setKidDetailData(it1)
                    setTaskData(it1.activeTasks.assignments)
                }
            }

            activeTasks.observe(viewLifecycleOwner) {
                it?.let { taskResponse -> setTaskData(taskResponse.assignments) }
            }

            getListRewardsSuccess.observe(viewLifecycleOwner) {
                setRewardData()
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()

        AppPreference.getTempChildId().let {
            if (it == emptyInt) {
                onBackPressed()
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

                tvLabelSwipe.isVisible = false
                rvProfileTask.isVisible = false
                cvAddTask.apply {
                    isVisible = true
                    setOnClickListener(onClickCallback)
                }
            } else {
                AppPreference.putActiveCycle(true)
                AppPreference.putTempChallengeStartDate(taskData[0].assignDate)

                val groupedActiveTask = mutableListOf<Pair<AssignmentsModel, List<TaskStarModel>>>()
//                val taskStarList = arrayListOf<TaskStarModel>()
//                taskData.distinctBy { it.globalChallengeId }.forEach {
//                    groupedActiveTask.add(it) //yang custom task yang ikutan cuman 1, karna nge distinctBy,
//                    // kalo globalChallengeId==null maka semua custom task dijadikan 1, logic harus di ubah
//                    taskStarList.add(TaskStarModel(isCompleted = it.completedAt != null))
//                }
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
                        // add
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
                        selectedPosition = viewHolder.adapterPosition
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

    private fun setRewardData() {
        dataBinding.apply {
            cvAddReward.apply {
                isVisible = true
                setOnClickListener(onClickCallback)
            }
        }
    }

    /*private fun initInfoDialog(textTitle: String, textDesc: String) {
        dialogInfoBinding = DialogInfoBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        val customSnackBar =
            Snackbar.make(dataBinding.clFamilyMainContainer, "", Snackbar.LENGTH_LONG)
        val layout = customSnackBar.view as Snackbar.SnackbarLayout

        dialogInfoBinding.apply {
            tvTitle.textOrNull(textTitle)
            tvDesc.textOrNull(textDesc)
            clInfo.setOnClickListener {
                customSnackBar.dismiss()
            }
        }

        val view: View = customSnackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        layout.setPadding(0, 0, 0, 0)
        layout.setBackgroundColor(context.color(R.color.transparent))
        layout.elevation = 0F
        layout.addView(dialogInfoBinding.root, 0)
        customSnackBar.show()
    }*/

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