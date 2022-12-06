package com.stellkey.android.view.carer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAddTaskDetailBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import kotlinx.android.synthetic.main.fragment_add_task_detail.view.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AddTaskDetailFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentAddTaskDetailBinding

    //private val binding by viewBinding<FragmentAddTaskDetailBinding>()
    private val viewModel by inject<ProfileViewModel>()

    private var tempCurrentDate: String? = null
    private var startDate: String? = null

    companion object {
        @JvmStatic
        fun newInstance() = AddTaskDetailFragment()
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
                popToInitialFragment()
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()

        viewModel.getDetailKid(profileId = AppPreference.getTempChildId())
        viewModel.getChallengeCategory()

        dataBinding.apply {
            tvTaskName.textOrNull = AppPreference.getTempSelectedChallengeName()
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
        }
    }

    private fun setKidDetailData(kidData: AllKidsModel) {
        dataBinding.apply {
            ivKidAvatar.loadImage(
                kidData.profileIcon.icon,
                ImageCornerOptions.ROUNDED,
                100
            )
            tvKidName.textOrNull = kidData.name
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
                val kidIdList = arrayListOf<Int>()
                kidIdList.add(AppPreference.getTempChildId())
                if (AppPreference.hasActiveCycle())
                    viewModel.postCreateAssignment(
                        CreateAssignmentRequest(
                            challengeId = null,
                            globalChallengeId = AppPreference.getTempSelectedGlobalId(),
                            kidId = kidIdList,
                            startDate = emptyString
                        )
                    )
                else
                    viewModel.postCreateAssignment(
                        CreateAssignmentRequest(
                            challengeId = null,
                            globalChallengeId = AppPreference.getTempSelectedGlobalId(),
                            kidId = kidIdList,
                            startDate = if (tempCurrentDate == startDate) emptyString else startDate.orEmpty()
                                .toString()
                        )
                    )

            }
            dataBinding.tvCancel -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.boxStartDate.editText -> initDatePicker(startDate)
        }
    }

}