package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentEditPINBinding
import com.stellkey.android.helper.extension.emptyBoolean
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import org.koin.android.ext.android.inject

class EditPINFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentEditPINBinding

    //private val binding by viewBinding<FragmentEditPINBinding>()
    private val viewModel by inject<AccountViewModel>()

    private var tempProfilePIN = emptyString

    companion object {
        @JvmStatic
        fun newInstance() = EditPINFragment()
        var currentProfilePIN = emptyString
        var isFromEditProfile = emptyBoolean
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_edit_p_i_n,
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

        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {
        onBackPressed()

        dataBinding.apply {
            when (AppPreference.getProfileType()) {
                Constant.FamilyType.CARERS -> {
                    tvTitle.textOrNull =
                        requireContext().resources.getString(R.string.edit_carer_pin_title)
                }
                Constant.FamilyType.KIDS -> {
                    tvTitle.textOrNull =
                        requireContext().resources.getString(R.string.edit_kid_pin_title)
                }
            }
        }
    }

    private fun validateForm() {
        dataBinding.apply {
            if (tempProfilePIN.isNotEmpty() && tempProfilePIN.length == 4) {
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

    private fun popToInitialFragment() {
        val backStackCount: Int = requireActivity().supportFragmentManager.backStackEntryCount
        val backStackLimit: Int = if(isFromEditProfile) 3 else 2

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
            AppPreference.deleteTempCreateMemberData()
            requireActivity().supportFragmentManager.popBackStack()
        }
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
            btnNext.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                AppPreference.deleteTempCreateMemberData()

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
            dataBinding.btnNext -> {
                dataBinding.apply {
                    if (currentProfilePIN == tempProfilePIN) {
                        clErrorMessage.visibility = View.INVISIBLE
                        addFragment(EditPINInputNewFragment.newInstance())
                        EditPINInputNewFragment.isFromEditProfile = isFromEditProfile

                    } else clErrorMessage.visibility = View.VISIBLE
                }
            }
            dataBinding.tvCancel -> {
                AppPreference.putEditProfile(emptyBoolean)
                popToInitialFragment()
            }
        }
    }
}