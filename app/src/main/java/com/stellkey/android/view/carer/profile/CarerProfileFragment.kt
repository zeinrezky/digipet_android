package com.stellkey.android.view.carer.profile

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.stellkey.android.R
import com.stellkey.android.databinding.DialogInfoBinding
import com.stellkey.android.databinding.FragmentCarerProfileBinding
import com.stellkey.android.helper.extension.color
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.view.carer.family.FamilyViewModel
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import org.koin.android.ext.android.inject

class CarerProfileFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentCarerProfileBinding

    //private val binding by viewBinding<FragmentCarerProfileBinding>()
    private val viewModel by inject<FamilyViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = CarerProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_carer_profile,
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

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
    }

    /*private fun initInfoDialog(textTitle: String, textDesc: String) {
        dialogInfoBinding = DialogInfoBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        val customSnackBar =
            Snackbar.make(dataBinding.clFamilyMainContainer, "", Snackbar.LENGTH_LONG)
        val layout = customSnackBar.view as Snackbar.SnackbarLayout

        dialogInfoBinding.apply {
            tvTitle.textOrNull(textTitle)
            tvDesc.textOrNull(textDesc)
            clInfo.setOnClickListener {
                customSnackBar.dismiss()
            }
        }

        val view: View = customSnackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        layout.setPadding(0, 0, 0, 0)
        layout.setBackgroundColor(context.color(R.color.transparent))
        layout.elevation = 0F
        layout.addView(dialogInfoBinding.root, 0)
        customSnackBar.show()
    }*/

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            cvTasks.setOnClickListener(onClickCallback)
            cvRewards.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.cvTasks -> {
                dataBinding.apply {
                    cvTasks.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    tvTasks.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    cvRewards.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                    tvRewards.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }
            }
            dataBinding.cvRewards -> {
                dataBinding.apply {
                    cvTasks.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey
                        )
                    )
                    tvTasks.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    cvRewards.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    tvRewards.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
        }
    }

}