package com.stellkey.android.view.carer.family.addcarer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogCancelAddMemberBinding
import com.stellkey.android.databinding.FragmentAddCarerConfirmPINBinding
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.request.CreateCarerRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.family.FamilyFragment
import com.stellkey.android.view.carer.family.FamilyViewModel
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_carer_confirm_p_i_n.*
import org.koin.android.ext.android.inject

class AddCarerConfirmPINFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentAddCarerConfirmPINBinding

    //private val binding by viewBinding<FragmentAddCarerConfirmPINBinding>()
    private val viewModel by inject<FamilyViewModel>()

    private var tempProfilePIN = emptyString

    private lateinit var dialogCancelBinding: DialogCancelAddMemberBinding
    private lateinit var cancelDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = AddCarerConfirmPINFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_carer_confirm_p_i_n,
                container,
                false
            )
        return dataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { bool ->
                bool.let { loading ->
                    if (loading) {
                        showWaitingDialog()
                    } else {
                        hideWaitingDialog()
                    }
                }
                createCarerSuccess.observe(viewLifecycleOwner) {
                    requireActivity().supportFragmentManager.fragments.let {
                        for (fragment in requireActivity().supportFragmentManager.fragments) {
                            requireActivity().supportFragmentManager.beginTransaction()
                                .remove(fragment)
                                .commit()
                        }
                        requireActivity().supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                    FamilyFragment.isAddCarer = true
                    FamilyFragment.addedCarerTempName = AppPreference.getTempCarerName()
                    AppPreference.deleteTempCreateMemberData()

                    (activity as HomeAct).showMenu(isShow = true)
                    addFragment(FamilyFragment.newInstance())
                }
            }
        }

        setView()
        setOnClick()
    }

    private fun setView() {}

    private fun validateForm() {
        if (AppPreference.getTempCarerPIN() == tempProfilePIN) {
            clErrorMessage.visibility = View.INVISIBLE
            viewModel.postCreateCarer(
                CreateCarerRequest(
                    name = AppPreference.getTempCarerName(),
                    pin = AppPreference.getTempCarerPIN(),
                    canCreate = true,
                    iconId = AppPreference.getProfileIconId()
                )
            )
        } else clErrorMessage.visibility = View.VISIBLE
    }

    private fun initCancelDialog() {
        dialogCancelBinding = DialogCancelAddMemberBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        cancelDialog = requireActivity().alertDialog(
            view = dialogCancelBinding.root,
            isCancelable = false,
            fullScreen = false
        )
        cancelDialog.window?.setBackgroundDrawableResource(R.color.transparent)

        dialogCancelBinding.tvDesc.textOrNull =
            requireContext().getString(R.string.dialog_add_member_cancel_desc_kid)

        dialogCancelBinding.btnCancel.setOnClickListener {
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
            cancelDialog.dismiss()
            (activity as HomeAct).showMenu(isShow = true)
            addFragment(FamilyFragment.newInstance())
        }

        dialogCancelBinding.btnContinue.setOnClickListener {
            cancelDialog.dismiss()
        }

        cancelDialog.show()
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            tvInputDigit1.setOnClickListener(onClickCallback)
            tvInputDigit2.setOnClickListener(onClickCallback)
            tvInputDigit3.setOnClickListener(onClickCallback)
            tvInputDigit4.setOnClickListener(onClickCallback)
            tvInputDigit5.setOnClickListener(onClickCallback)
            tvInputDigit6.setOnClickListener(onClickCallback)
            tvInputDigit7.setOnClickListener(onClickCallback)
            tvInputDigit8.setOnClickListener(onClickCallback)
            tvInputDigit9.setOnClickListener(onClickCallback)
            tvInputDigit0.setOnClickListener(onClickCallback)
            ivDelete.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.tvInputDigit1 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("1")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("1")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("1")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("1")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }

            }
            dataBinding.tvInputDigit2 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("2")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("2")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("2")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("2")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit3 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("3")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("3")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("3")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("3")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit4 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("4")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("4")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("4")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("4")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit5 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("5")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("5")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("5")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("5")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit6 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("6")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("6")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("6")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("6")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit7 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("7")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("7")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("7")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("7")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit8 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("8")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("8")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("8")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("8")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit9 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("9")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("9")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("9")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("9")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.tvInputDigit0 -> {
                dataBinding.apply {
                    if (etFirstDigit.text.toString().isEmpty()) {
                        etFirstDigit.setText("0")
                        tempProfilePIN = etFirstDigit.text.toString()
                    } else if (etSecondDigit.text.toString().isEmpty()) {
                        etSecondDigit.setText("0")
                        tempProfilePIN = tempProfilePIN.plus(etSecondDigit.text.toString())
                    } else if (etThirdDigit.text.toString().isEmpty()) {
                        etThirdDigit.setText("0")
                        tempProfilePIN = tempProfilePIN.plus(etThirdDigit.text.toString())
                    } else if (etFourthDigit.text.toString().isEmpty()) {
                        etFourthDigit.setText("0")
                        tempProfilePIN = tempProfilePIN.plus(etFourthDigit.text.toString())
                        validateForm()
                    }
                }
            }
            dataBinding.ivDelete -> {
                dataBinding.apply {
                    if (etFourthDigit.text.toString().isNotEmpty()) {
                        etFourthDigit.text.clear()
                        tempProfilePIN = StringBuilder(tempProfilePIN).deleteAt(3).toString()
                    } else if (etThirdDigit.text.toString().isNotEmpty()) {
                        etThirdDigit.text.clear()
                        tempProfilePIN = StringBuilder(tempProfilePIN).deleteAt(2).toString()
                    } else if (etSecondDigit.text.toString().isNotEmpty()) {
                        tempProfilePIN = StringBuilder(tempProfilePIN).deleteAt(1).toString()
                        etSecondDigit.text.clear()
                    } else if (etFirstDigit.text.toString().isNotEmpty()) {
                        tempProfilePIN = StringBuilder(tempProfilePIN).deleteAt(0).toString()
                        etFirstDigit.text.clear()
                    }
                }
            }
            dataBinding.tvCancel -> {
                initCancelDialog()
            }
        }
    }

}