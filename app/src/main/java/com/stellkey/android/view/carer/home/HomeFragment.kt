package com.stellkey.android.view.carer.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentHomeBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.request.AssignmentActionRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.adapter.HomeUserAdapter
import com.stellkey.android.view.carer.home.adapter.TodayTaskAdapter
import com.stellkey.android.view.carer.home.adapter.YesterdayTaskAdapter
import com.stellkey.android.view.intro.auth.LoginChooseProfileFragment
import com.stellkey.android.view.intro.intro.IntroAct
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment(), TodayTaskAdapter.Listener, YesterdayTaskAdapter.Listener {

    private lateinit var dataBinding: FragmentHomeBinding

    //private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by inject<HomeViewModel>()

    private lateinit var yesterdayTaskAdapter: YesterdayTaskAdapter
    private lateinit var todayTaskAdapter: TodayTaskAdapter
    private lateinit var userSliderAdapter: HomeUserAdapter

    private var allKidsList = arrayListOf<AllKidsModel>()
    private var activeTaskList = arrayListOf<AssignmentsModel>()
    private var kidId = emptyInt

    private var isYesterdayListEmpty = emptyBoolean
    private var isTodayListEmpty = emptyBoolean

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
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
            responseError.observe(viewLifecycleOwner) {
                AppPreference.deleteAll()
                val intent = Intent(context, IntroAct::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                requireActivity().finish()
            }

            listAllKids.observe(viewLifecycleOwner) {
                setAllKidsList(it)
            }

            yesterdayAssignment.observe(viewLifecycleOwner) {
                it?.let { it1 -> setYesterdayTaskList(it1) }
            }

            todayAssignment.observe(viewLifecycleOwner) {
                it?.let { it1 -> setTodayTaskList(it1) }
            }

            declineKidTaskCompletionSuccess.observe(viewLifecycleOwner) {
                /*todayTaskAdapter.removeNotification()*/
                viewModel.getTodayAssignment(kidId)
            }

            confirmKidTaskCompletionSuccess.observe(viewLifecycleOwner) {
                /*todayTaskAdapter.removeNotification()*/
                viewModel.getTodayAssignment(kidId)
            }

            declineKidTaskWithoutCompletionSuccess.observe(viewLifecycleOwner) {
                /*yesterdayTaskAdapter.removeNotification()*/
                viewModel.getTodayAssignment(kidId)
            }

            confirmKidTaskWithoutCompletionSuccess.observe(viewLifecycleOwner) {
                /*yesterdayTaskAdapter.removeNotification()*/
                viewModel.getTodayAssignment(kidId)
            }

        }

        (activity as HomeAct).showMenu(isShow = true)
        setView()
        setOnClick()
    }

    private fun setView() {
        viewModel.getListAllKids()

        onBackPressed()
        validateListVisibility()
    }

    private fun setAllKidsList(listData: ArrayList<AllKidsModel>) {
        allKidsList = listData
        dataBinding.apply {
            if (allKidsList.isNotEmpty()) {
                if (allKidsList.size == 1) {
                    clAvatar.isVisible = true
                    clMultiUserHome.isVisible = false

                    setKidData(allKidsList[0])
                } else {
                    clAvatar.isVisible = false
                    clMultiUserHome.isVisible = true

                    userSliderAdapter = HomeUserAdapter(requireContext(), allKidsList)
                    vpUserHome.apply {
                        adapter = userSliderAdapter
                    }
                    indicatorUserHome.attachTo(vpUserHome)
                    setViewPager()
                }
            } else {
                clAvatar.isVisible = false
                clMultiUserHome.isVisible = false
                tvTodayTask.isVisible = false
                tvCurrentCycleRange.isVisible = false
                rvTodayTask.isVisible = false
            }
        }
    }

    private fun setKidData(data: AllKidsModel?) {
        if (data != null) {
            dataBinding.apply {
                kidId = data.id
                ivAvatar.loadImage(data.profileIcon.icon, ImageCornerOptions.ROUNDED, 100)
                tvProfileName.textOrNull = data.name
                tvCurrentCycleRange.textOrNull = data.activeAssignments.dateRange

                setTodayTaskList(data.tasksToday)
            }
        }
    }

    private fun setYesterdayTaskList(assignmentsResponse: AllKidsModel.Assignments?) {
        dataBinding.apply {
            if (assignmentsResponse?.assignments?.isEmpty() == true) {
                isYesterdayListEmpty = true
            } else {
                isYesterdayListEmpty = false
                activeTaskList = assignmentsResponse?.assignments.orEmpty().toArrayList()

                yesterdayTaskAdapter = YesterdayTaskAdapter(
                    requireContext(),
                    assignmentsResponse?.assignments.orEmpty().toArrayList(),
                    activeTaskList,
                    this@HomeFragment
                )

                rvYesterdayTask.apply {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = yesterdayTaskAdapter
                }
            }

            validateListVisibility()

        }
    }

    private fun setTodayTaskList(assignmentsResponse: AllKidsModel.Assignments?) {
        dataBinding.apply {
            if (assignmentsResponse?.assignments?.isEmpty() == true) {
                isTodayListEmpty = true
            } else {
                isTodayListEmpty = false

                todayTaskAdapter = TodayTaskAdapter(
                    requireContext(),
                    assignmentsResponse?.assignments.orEmpty().toArrayList(),
                    this@HomeFragment
                )

                rvTodayTask.apply {
                    layoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = todayTaskAdapter
                }
            }

            validateListVisibility()
        }
    }

    private fun setViewPager() {
        dataBinding.vpUserHome.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setKidData(allKidsList[position])
                    viewModel.getYesterdayAssignment(allKidsList[position].id)
                }
            })
    }

    private fun validateListVisibility() {
        dataBinding.apply {
            if (isYesterdayListEmpty) {
                clYesterdayTasks.isVisible = false
            } else {
                clYesterdayTasks.isVisible = true
                tvNoApproval.isVisible = false
            }

            if (isTodayListEmpty) {
                clTodayTasks.isVisible = false
            } else {
                clTodayTasks.isVisible = true
                tvNoApproval.isVisible = false
            }

            if (isTodayListEmpty && isYesterdayListEmpty) {
                tvCurrentCycleRange.isVisible = false
                clYesterdayTasks.isVisible = false
                clTodayTasks.isVisible = false
                tvNoApproval.isVisible = true
            } else {
                tvCurrentCycleRange.isVisible = true
                tvNoApproval.isVisible = false
            }
        }
    }

    //Today Task
    override fun onTodayTaskItemClicked(data: AssignmentsModel) {}

    override fun onTodayTaskItemApproved(data: AssignmentsModel) {
        viewModel.postConfirmKidTaskCompletion(AssignmentActionRequest(assignmentId = data.id))
    }

    override fun onTodayTaskItemDeclined(data: AssignmentsModel) {
        viewModel.postDeclineKidTaskCompletion(AssignmentActionRequest(assignmentId = data.id))
    }

    //Yesterday Task
    override fun onYesterdayTaskItemClicked(data: AssignmentsModel) {

    }

    override fun onYesterdayTaskItemApproved(data: AssignmentsModel) {
        viewModel.postConfirmKidTaskWithoutCompletion(AssignmentActionRequest(assignmentId = data.id))
    }

    override fun onYesterdayTaskItemDeclined(data: AssignmentsModel) {
        viewModel.postDeclineKidTaskWithoutCompletion(AssignmentActionRequest(assignmentId = data.id))
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            cvSwitchUser.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.cvSwitchUser -> {
                (activity as HomeAct).showMenu(isShow = false)
                addFragment(LoginChooseProfileFragment.newInstance())
            }
        }
    }

}