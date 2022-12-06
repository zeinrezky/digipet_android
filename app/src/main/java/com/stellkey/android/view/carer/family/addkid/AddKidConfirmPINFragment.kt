package com.stellkey.android.view.carer.family.addkid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogCancelAddMemberBinding
import com.stellkey.android.databinding.FragmentAddKidConfirmPINBinding
import com.stellkey.android.helper.extension.alertDialog
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.family.FamilyFragment
import kotlinx.android.synthetic.main.fragment_add_kid_confirm_p_i_n.*

class AddKidConfirmPINFragment : BaseFragment() {

    private val binding by viewBinding<FragmentAddKidConfirmPINBinding>()
    private var tempProfilePIN = emptyString

    private lateinit var dialogCancelBinding: DialogCancelAddMemberBinding
    private lateinit var cancelDialog: AlertDialog

    companion object {
        @JvmStatic
        fun newInstance() = AddKidConfirmPINFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_kid_confirm_p_i_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {}

    private fun validateForm() {
        if (AppPreference.getTempChildProfilePIN() == tempProfilePIN) {
            clErrorMessage.visibility = View.INVISIBLE
            addFragment(AddKidAgeFragment.newInstance())
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
        binding.apply {
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
            binding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            binding.tvInputDigit1 -> {
                binding.apply {
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
            binding.tvInputDigit2 -> {
                binding.apply {
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
            binding.tvInputDigit3 -> {
                binding.apply {
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
            binding.tvInputDigit4 -> {
                binding.apply {
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
            binding.tvInputDigit5 -> {
                binding.apply {
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
            binding.tvInputDigit6 -> {
                binding.apply {
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
            binding.tvInputDigit7 -> {
                binding.apply {
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
            binding.tvInputDigit8 -> {
                binding.apply {
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
            binding.tvInputDigit9 -> {
                binding.apply {
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
            binding.tvInputDigit0 -> {
                binding.apply {
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
            binding.ivDelete -> {
                binding.apply {
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
            binding.tvCancel -> {
                initCancelDialog()
            }
        }
    }

}