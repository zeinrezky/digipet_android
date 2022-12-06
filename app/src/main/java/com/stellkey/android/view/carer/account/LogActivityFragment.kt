package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogDeleteAllLogBinding
import com.stellkey.android.databinding.FragmentLogActivityBinding
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.emptyInt
import com.stellkey.android.model.request.DeleteLogRequest
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject

class LogActivityFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentLogActivityBinding

    //private val binding by viewBinding<FragmentLogActivityBinding>()
    private val viewModel by inject<AccountViewModel>()

    private lateinit var dialogDeleteAllLogBinding: DialogDeleteAllLogBinding
    private lateinit var deleteAllLogDialog: AlertDialog

    private var selectedLogAmount = emptyInt

    companion object {
        @JvmStatic
        fun newInstance() = LogActivityFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_log_activity,
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
            deleteLogSuccess.observe(viewLifecycleOwner) {
                requireActivity().supportFragmentManager.popBackStack()
            }

        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {
        onBackPressed()

        dataBinding.apply {
            rgDeleteLogOptions.setOnCheckedChangeListener { group, checkedId ->
                val id: Int = rgDeleteLogOptions.checkedRadioButtonId
                if (id != emptyInt) {
                    // If any radio button checked from radio group
                    // Get the instance of radio button using id
                    btnSave.apply {
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
                } else {
                    // If no radio button checked in this radio group
                    btnSave.apply {
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

                when (id) {
                    R.id.rbLastDay -> {
                        selectedLogAmount = 1
                    }
                    R.id.rbLastWeek -> {
                        selectedLogAmount = 7
                    }
                    R.id.rbLastMonth -> {
                        selectedLogAmount = 28
                    }
                    R.id.rbAll -> {
                        selectedLogAmount = 0
                    }
                    else -> {
                        selectedLogAmount = emptyInt
                    }
                }

            }
        }
    }

    private fun initDeleteAllLogDialog() {
        dialogDeleteAllLogBinding = DialogDeleteAllLogBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        deleteAllLogDialog = requireActivity().alertDialog(
            view = dialogDeleteAllLogBinding.root,
            isCancelable = true,
            fullScreen = true
        )
        deleteAllLogDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogDeleteAllLogBinding.btnYes.setOnClickListener {
            viewModel.deleteLog(DeleteLogRequest(days = selectedLogAmount))
            deleteAllLogDialog.dismiss()
        }

        dialogDeleteAllLogBinding.btnNo.setOnClickListener {
            deleteAllLogDialog.dismiss()
        }

        deleteAllLogDialog.show()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            (activity as HomeAct).showMenu(isShow = true)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.btnSave -> {
                if (selectedLogAmount == 0)
                    initDeleteAllLogDialog()
                else
                    viewModel.deleteLog(DeleteLogRequest(days = selectedLogAmount))
            }
            dataBinding.tvCancel -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

}