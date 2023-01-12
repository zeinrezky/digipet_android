package com.stellkey.android.view.carer.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogConfirmationCustomTaskBinding
import com.stellkey.android.databinding.FragmentAddTaskDetailBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.defaultDateFormat
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.formatDate
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.orEmpty
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.helper.extension.timePicker
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.model.request.CustomTaskRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.profile.ProfileViewModel
import com.stellkey.android.view.carer.task.adapter.ActionButtonAdapter
import com.stellkey.android.view.carer.task.adapter.KidProfileAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import org.koin.android.ext.android.inject

class AddTaskDetailFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentAddTaskDetailBinding

    private lateinit var dialogConfirmationAssignCycle: DialogConfirmationCustomTaskBinding
    private lateinit var confirmationDialog: AlertDialog

    private val viewModel by inject<ProfileViewModel>()

    private var tempCurrentDate: String? = null
    private var startDate: String? = null
    private val listAllKids = ArrayList<AllKidsModel>()
    private val tempSelectedList = ArrayList<AllKidsModel>()
    private val tempUnselectedList = ArrayList<AllKidsModel>()

    private val kidSelectedProfileAdapter by lazy {
        KidProfileAdapter()
    }
    private val kidProfileAdapter by lazy {
        KidProfileAdapter(listener = onKidClicked)
    }

    private val addDoneIconAdapter by lazy {
        ActionButtonAdapter(
            type = ActionButtonAdapter.ButtonActionType.ADD_ICON,
            listener = onAddIconClicked
        )
    }

    private val listKidThatNotHaveCycleYet = arrayListOf<String>()

    companion object {

        @JvmStatic
        fun newInstance() = AddTaskDetailFragment()

        var isCustomTask = false
        var isGlobalTask = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_task_detail,
                container,
                false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAllKids.clear()
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

            challengeCategories.observe(viewLifecycleOwner) {
                dataBinding.apply {
                    it?.forEach {
                        if (it.id == AppPreference.getTempSelectedCategoryId())
                            tvCategoryName.textOrNull = it.title
                    }
                }
            }

            detailKid.observe(viewLifecycleOwner) {
                it?.let { it1 ->
                    setKidDetailData(it1)
                }
            }

            createAssignmentSuccess.observe(viewLifecycleOwner) {
                if (listKidThatNotHaveCycleYet.isNotEmpty()) {
                    initDialogConfirmationAssignCycle(
                        listKidThatNotHaveCycleYet.joinToString(separator = ", "),
                        dataBinding.etStartDate.text.toString()
                    )
                } else {
                    popToInitialFragment()
                }
            }

            createNewTaskSuccess.observe(viewLifecycleOwner) {
                it?.let { customTask ->
                    AppPreference.putTempSelectedChallengeId(customTask.id)
                    createTaskForExistingTask()
                }
            }

            listKids.observe(viewLifecycleOwner) {
                it?.let { kids ->
                    listAllKids.addAll(kids.map { kid ->
                        kid.apply {
                            uiAction = AllKidsModel.UIAction(
                                isSelected = false,
                                isEnable = checkIsKidEligibleToAssignment(kid)
                            )
                        }
                    }.filter { kid -> kid.id != AppPreference.getTempChildId() })
                }
            }
        }

        setView()
        setOnClick()
    }

    private fun initDialogConfirmationAssignCycle(kidsName: String, cycleDate: String) {
        dialogConfirmationAssignCycle = DialogConfirmationCustomTaskBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        confirmationDialog = requireActivity().alertDialog(
            view = dialogConfirmationAssignCycle.root,
            isCancelable = false,
            fullScreen = false
        )
        confirmationDialog.window?.setBackgroundDrawableResource(R.color.blurWhite)

        dialogConfirmationAssignCycle.tvDesc.textOrNull = requireContext().getString(
            R.string.dialog_confirmation_custom_task_desc,
            kidsName,
            cycleDate
        )
        dialogConfirmationAssignCycle.btnContinue.setOnClickListener {
            popToInitialFragment()
            confirmationDialog.dismiss()
        }

        confirmationDialog.show()
    }

    private fun checkIsKidEligibleToAssignment(kid: AllKidsModel): Boolean {
        if (kid.tasksToday.assignments.size < 3 && isCustomTask) {
            return true
        } else if (kid.tasksToday.assignments.size < 3) {
            val assignmentId =
                if (isGlobalTask) AppPreference.getTempSelectedGlobalChallengeId() else AppPreference.getTempSelectedChallengeId()
            var isEligible = true
            kid.tasksToday.assignments.forEach {
                if (it.globalChallengeId == assignmentId || it.challengeId == assignmentId) {
                    isEligible = false
                    return@forEach
                }
            }
            return isEligible
        } else {
            return false
        }
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()

        viewModel.getDetailKid(profileId = AppPreference.getTempChildId())
        viewModel.getListAllKids()
        viewModel.getChallengeCategory()

        dataBinding.apply {
            if (isCustomTask) {
                boxTitleTask.isVisible = true
                tvTaskName.isGone = true
                etTitleTask.afterTextChanged { titleTask ->
                    AppPreference.putTempSelectedChallengeName(titleTask)
                }
            } else {
                boxTitleTask.isGone = true
                tvTaskName.isVisible = true
                tvTaskName.textOrNull = AppPreference.getTempSelectedChallengeName()
            }
            ivChallenge.loadImage(
                AppPreference.getTempSelectedChallengeIcon(),
                ImageCornerOptions.ROUNDED,
                30
            )
            if (AppPreference.hasActiveCycle()) {
                boxStartDate.setOnClickListener(null)
                val current = AppPreference.getTempChallengeStartDate()
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val formatted = parser.parse(current)?.let { formatter.format(it) }
                etStartDate.textOrNull = formatted
            } else {
                boxStartDate.editText?.setOnClickListener(onClickCallback)
            }

            rvKids.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter =
                    ConcatAdapter(kidSelectedProfileAdapter, kidProfileAdapter, addDoneIconAdapter)
            }
        }
    }

    private fun setKidDetailData(kidData: AllKidsModel) {
        dataBinding.apply {
            kidSelectedProfileAdapter.setProfileListInto(arrayListOf(kidData.apply {
                uiAction = AllKidsModel.UIAction(isSelected = true, isEnable = true)
            }))
        }
    }

    private fun initDatePicker(selectedDate: String? = null) {
        val day: Long = 86400000
        val currentDateMillis = Date().time
        var selectedStartDate: Long? = null

        if (selectedDate != null) {
            val date: Date =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDate) as Date
            selectedStartDate = date.time
        }

        childFragmentManager.timePicker(
            selection = if (selectedDate.isNullOrEmpty()) currentDateMillis else selectedStartDate?.plus(
                day
            ),
            title = "Start Date",
        ) { selectionMillis ->
            val todayDate = defaultDateFormat.format(Calendar.getInstance().time)
            val formattedTodayDate = todayDate.formatDate(
                from = "yyyy-MM-dd",
                to = "dd/MM/yyyy"
            )

            val currentDate = defaultDateFormat.format(Date(selectionMillis))
            val formattedDate = currentDate.formatDate(
                from = "yyyy-MM-dd",
                to = "dd/MM/yyyy"
            )

            tempCurrentDate = formattedTodayDate
            startDate = currentDate
            dataBinding.etStartDate.textOrNull = formattedDate
        }
    }

    private fun popToInitialFragment() {
        val backStackCount: Int = requireActivity().supportFragmentManager.backStackEntryCount
        val backStackLimit = 3

        for (i in (backStackCount - 1) downTo backStackLimit) {

            // Get the back stack fragment id.
            val backStackId: Int =
                requireActivity().supportFragmentManager.getBackStackEntryAt(i).id
            requireActivity().supportFragmentManager.popBackStack(
                backStackId,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            btnCreate.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.btnCreate -> {
                if (isCustomTask) {
                    createTaskForNewTask()
                } else {
                    createTaskForExistingTask()
                }
            }

            dataBinding.tvCancel -> {
                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.boxStartDate.editText -> initDatePicker(startDate)
        }
    }

    private val onKidClicked = object : KidProfileAdapter.Listener {
        override fun onKidProfileSelected(data: AllKidsModel) {
            listAllKids.find { kid -> kid.id == data.id }.apply {
                this?.uiAction?.isSelected = this?.uiAction?.isSelected != true
            }
            refreshKidProfileAdapter()
        }
    }

    private val onAddIconClicked = object : ActionButtonAdapter.Listener {
        override fun onActionButtonClicked(type: ActionButtonAdapter.ButtonActionType) {
            if (type == ActionButtonAdapter.ButtonActionType.ADD_ICON) {
                showAllKid()
            } else {
                showSelectedKidOnly()
            }
        }
    }

    private fun refreshKidProfileAdapter() {
        kidProfileAdapter.notifyDataSetChanged()
    }

    private fun showAllKid() {
        addDoneIconAdapter.setIconType(ActionButtonAdapter.ButtonActionType.DONE_ICON)
        tempSelectedList.clear()
        tempUnselectedList.clear()

        tempSelectedList.addAll(listAllKids.filter { kid -> kid.uiAction.isSelected })
        tempUnselectedList.addAll(listAllKids.filter { kid -> !kid.uiAction.isSelected })

        val combinedList = tempSelectedList + tempUnselectedList
        kidProfileAdapter.setProfileListInto(combinedList.distinctBy { it.id }.toArrayList())
    }

    private fun showSelectedKidOnly() {
        addDoneIconAdapter.setIconType(ActionButtonAdapter.ButtonActionType.ADD_ICON)
        tempSelectedList.clear()
        tempUnselectedList.clear()

        tempSelectedList.addAll(listAllKids.filter { kid -> kid.uiAction.isSelected })
        kidProfileAdapter.setProfileListInto(tempSelectedList.distinctBy { it.id }.toArrayList())
    }

    private fun createTaskForNewTask() {
        viewModel.postNewTaskAssignment(
            request = CustomTaskRequest(
                challengeCatId = AppPreference.getTempSelectedCategoryId(),
                title = AppPreference.getTempSelectedChallengeName()
            )
        )
    }

    private fun createTaskForExistingTask() {
        val kidIdList = arrayListOf<Int>()
        kidIdList.add(AppPreference.getTempChildId())
        listAllKids.forEach {
            if (it.uiAction.isSelected) {
                kidIdList.add(it.id)
                if (it.activeAssignments.assignments.isEmpty()) {
                    listKidThatNotHaveCycleYet.add("${it.name}'s")
                }
            }
        }

        if (AppPreference.hasActiveCycle()) {
            viewModel.postCreateAssignment(
                CreateAssignmentRequest(
                    challengeId = if (isGlobalTask) null else AppPreference.getTempSelectedChallengeId(),
                    globalChallengeId = if (isGlobalTask) AppPreference.getTempSelectedGlobalChallengeId() else null,
                    kidId = kidIdList,
                    startDate = emptyString
                )
            )
        } else {
            viewModel.postCreateAssignment(
                CreateAssignmentRequest(
                    challengeId = if (isGlobalTask) null else AppPreference.getTempSelectedChallengeId(),
                    globalChallengeId = if (isGlobalTask) AppPreference.getTempSelectedGlobalChallengeId() else null,
                    kidId = kidIdList,
                    startDate = if (tempCurrentDate == startDate) emptyString else startDate.orEmpty()
                        .toString()
                )
            )
        }
    }

}