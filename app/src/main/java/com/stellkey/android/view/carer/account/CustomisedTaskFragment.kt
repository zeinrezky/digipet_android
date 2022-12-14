package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentCustomisedTaskBinding
import com.stellkey.android.model.CustomChallengeModel
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.account.adapter.CustomizedTaskAdapter
import com.stellkey.android.view.carer.home.HomeAct
import org.koin.android.ext.android.inject

class CustomisedTaskFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentCustomisedTaskBinding

    //private val binding by viewBinding<FragmentCustomisedTaskBinding>()
    private val viewModel by inject<AccountViewModel>()

    private lateinit var customizedTaskAdapter: CustomizedTaskAdapter

    companion object {

        @JvmStatic
        fun newInstance() = CustomisedTaskFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_customised_task,
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

            customTasksResponse.observe(viewLifecycleOwner) {
                setupCustomizedRecyclerview(it)
            }
        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setupCustomizedRecyclerview(listTasks: List<CustomChallengeModel>) {
        customizedTaskAdapter =
            CustomizedTaskAdapter(listTasks, listener = onCustomChallengeClicked)
        dataBinding.rvCustomisedTask.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customizedTaskAdapter
        }
    }

    private fun setView() {
        viewModel.getListCustomChallenge()

        onBackPressed()
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
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private val onCustomChallengeClicked = object : CustomizedTaskAdapter.Listener {
        override fun onChallengeClicked(challenge: CustomChallengeModel) {
            addFragment(CustomisedTaskDetailFragment.newInstance(challenge))
        }
    }
}