package com.stellkey.android.view.carer.family.addkid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAddFirstTaskStartDateBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.family.FamilyFragment
import com.stellkey.android.view.carer.family.FamilyViewModel
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AddFirstTaskStartDateFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentAddFirstTaskStartDateBinding

    //private val binding by viewBinding<FragmentAddFirstTaskStartDateBinding>()
    private val viewModel by inject<FamilyViewModel>()

    private var tempCurrentDate: String? = null
    private var startDate: String? = null

    companion object {
        @JvmStatic
        fun newInstance() = AddFirstTaskStartDateFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_first_task_start_date,
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
            createAssignmentSuccess.observe(viewLifecycleOwner) {
                AppPreference.deleteTempCreateMemberData()
                requireActivity().supportFragmentManager.fragments.let {
                    for (fragment in requireActivity().supportFragmentManager.fragments) {
                        requireActivity().supportFragmentManager.beginTransaction().remove(fragment)
                            .commit()
                    }
                    requireActivity().supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                FamilyFragment.isAddKid = true
                (activity as HomeAct).showMenu(isShow = true)
                addFragment(FamilyFragment.newInstance())
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {
        validateForm()

        dataBinding.apply {
            etStartDate.afterTextChanged {
                validateForm()
            }
        }
    }

    private fun validateForm() {
        dataBinding.apply {
            if (!etStartDate.text.isNullOrEmpty()) {
                btnNext.apply {
                    isEnabled = true
                    isClickable = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    setOnClickListener(onClickCallback)
                }

            } else
                btnNext.apply {
                    isEnabled = false
                    isClickable = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primary_disabled
                        )
                    )
                    setOnClickListener(null)
                }
        }
    }

    private fun initDatePicker(selectedDate: String? = null) {
        val day: Long = 86400000
        val currentDateMillis = Date().time
        var selectedStartDate: Long? = null

        if (selectedDate != null) {
            val date: Date =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate)
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
            dataBinding.etStartDate.textOrNull(formattedDate)
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            boxStartDate.editText?.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.boxStartDate.editText -> initDatePicker(startDate)
            dataBinding.btnNext -> {
                dataBinding.apply {
                    val kidIdList = arrayListOf<Int>()
                    kidIdList.add(AppPreference.getTempChildId())

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
            }
        }
    }

}