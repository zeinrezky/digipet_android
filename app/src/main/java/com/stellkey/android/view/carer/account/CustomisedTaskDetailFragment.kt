package com.stellkey.android.view.carer.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentCustomisedTaskDetailBinding
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.model.ChallengeCategoryModel
import com.stellkey.android.model.CustomChallengeModel
import com.stellkey.android.model.request.EditCustomChallenge
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import org.koin.android.ext.android.inject

class CustomisedTaskDetailFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentCustomisedTaskDetailBinding
    private val viewModel by inject<AccountViewModel>()
    private var newCustomChallengeUpdate = EditCustomChallenge()

    companion object {

        private lateinit var selectedChallenge: CustomChallengeModel

        const val CHALLENGE_KEY = "CHALLENGE_KEY"

        @JvmStatic
        fun newInstance(challenge: CustomChallengeModel) = CustomisedTaskDetailFragment().apply {
            selectedChallenge = challenge
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_customised_task_detail,
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

            challengeCategories.observe(viewLifecycleOwner) { listChallenge ->
                listChallenge?.let {
                    mappingValueOfSelectedChallenge(selectedChallenge, it)
                }
            }

            successUpdateCustomChallenge.observe(viewLifecycleOwner) {
                requireActivity().supportFragmentManager.popBackStack()
            }

            successDeleteCustomChallenge.observe(viewLifecycleOwner) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        (activity as HomeAct).showMenu(isShow = false)
        setView()
        setOnClick()
    }

    private fun setView() {
        viewModel.getChallengeCategory()

        onBackPressed()
    }

    private fun mappingValueOfSelectedChallenge(
        challenge: CustomChallengeModel,
        listCategoryModel: ArrayList<ChallengeCategoryModel>
    ) {
        newCustomChallengeUpdate.apply {
            id = challenge.id
            title = challenge.title
            challengeCatId = challenge.challengeCatId
        }

        dataBinding.btnRemoveTask.isEnabled = true
        dataBinding.btnSave.isEnabled = true
        dataBinding.tvTitle.text = challenge.title
        dataBinding.ivChallenge.loadImage(challenge.challengeCat.icon)
        dataBinding.etTask.setText(challenge.title)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            listCategoryModel.map { it.title })
        dataBinding.spinnerTaskCategory.adapter = spinnerAdapter
        dataBinding.spinnerTaskCategory.setSelection(listCategoryModel.indexOf(listCategoryModel.find { it.title == challenge.challengeCat.title }))
        dataBinding.spinnerTaskCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    newCustomChallengeUpdate.challengeCatId = listCategoryModel[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        dataBinding.etTask.afterTextChanged {
            newCustomChallengeUpdate.title = it
        }
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
            btnSave.setOnClickListener(onClickCallback)
            btnRemoveTask.setOnClickListener(onClickCallback)
            tvCancel.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.tvCancel -> {
                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.btnSave -> {
                viewModel.editCustomChallenge(newCustomChallengeUpdate)
            }

            dataBinding.btnRemoveTask -> {
                viewModel.deleteCustomChallenge(newCustomChallengeUpdate.id)
            }
        }
    }
}