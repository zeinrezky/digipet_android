package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogFinishSetupKidProfileBinding
import com.stellkey.android.databinding.FragmentRegisterChallengeStartDateBinding
import com.stellkey.android.helper.extension.*
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class RegisterChallengeStartDateFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentRegisterChallengeStartDateBinding

    //private val binding by viewBinding<FragmentRegisterEmailBinding>()
    private val viewModel by inject<RegisterViewModel>()

    private var tempCurrentDate: String? = null
    private var startDate: String? = null

    private lateinit var dialogAddKidPopupBinding: DialogFinishSetupKidProfileBinding
    private lateinit var addKidPopupDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = RegisterChallengeStartDateFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_challenge_start_date,
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
                initAddKidDialog()
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
            dataBinding.etStartDate.textOrNull(formattedDate)
        }
    }

    private fun initAddKidDialog() {
        dialogAddKidPopupBinding = DialogFinishSetupKidProfileBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        addKidPopupDialog = requireActivity().alertDialog(
            view = dialogAddKidPopupBinding.root,
            isCancelable = false,
            fullScreen = false
        )
        addKidPopupDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogAddKidPopupBinding.btnNo.setOnClickListener {
            addKidPopupDialog.dismiss()
            addFragment(RegisterProfileSettingsFragment.newInstance())
        }

        addKidPopupDialog.show()
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