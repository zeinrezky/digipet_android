package com.stellkey.android.view.carer.family

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAddMemberOptionsBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.carer.family.addcarer.AddCarerFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.family.addkid.AddKidFragment
import org.koin.android.ext.android.inject

class AddMemberOptionsFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentAddMemberOptionsBinding

    //private val binding by viewBinding<FragmentAddMemberOptionsBinding>()
    private val viewModel by inject<FamilyViewModel>()

    companion object {
        @JvmStatic
        fun newInstance() = AddMemberOptionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_member_options,
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

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            cvAddCarer.setOnClickListener(onClickCallback)
            cvAddKid.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                AppPreference.deleteTempCreateMemberData()
                (activity as HomeAct).showMenu(isShow = true)
                requireActivity().supportFragmentManager.popBackStack()
            }
            dataBinding.cvAddCarer -> {
                addFragment(AddCarerFragment.newInstance())
            }
            dataBinding.cvAddKid -> {
                addFragment(AddKidFragment.newInstance())
            }
        }
    }

}