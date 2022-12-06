package com.stellkey.android.view.intro.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentRegisterProfilePINBinding
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment

class RegisterProfilePINFragment : BaseFragment() {

    private val binding by viewBinding<FragmentRegisterProfilePINBinding>()
    private var tempProfilePIN = emptyString

    companion object {
        @JvmStatic
        fun newInstance() = RegisterProfilePINFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_profile_p_i_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setOnClick()
    }

    private fun setView() {}

    private fun validateForm() {
        binding.apply {
            if (tempProfilePIN.isNotEmpty() && tempProfilePIN.length == 4) {
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
            } else
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
            binding.btnSave -> {
                AppPreference.putTempChildProfilePIN(tempProfilePIN)
                addFragment(RegisterConfirmProfilePINFragment.newInstance())
            }
        }
    }

}