package com.stellkey.android.view.carer.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAddRewardBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.afterTextChanged
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.RewardModel
import com.stellkey.android.model.request.CreateRewardRequest
import com.stellkey.android.model.request.RewardAssignKidRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.profile.ProfileViewModel
import com.stellkey.android.view.carer.profile.adapter.RecommendedRewardAdapter
import com.stellkey.android.view.carer.task.adapter.ActionButtonAdapter
import com.stellkey.android.view.carer.task.adapter.KidProfileAdapter
import org.koin.android.ext.android.inject

class AddRewardFragment : BaseFragment(), RecommendedRewardAdapter.Listener {

    private lateinit var dataBinding: FragmentAddRewardBinding

    //private val binding by viewBinding<FragmentAddRewardBinding>()
    private val viewModel by inject<ProfileViewModel>()

    private lateinit var recommendedRewardAdapter: RecommendedRewardAdapter
    private val listRewards = ArrayList<RewardModel>()
    private var selectedReward = RewardModel()
    private var selectedStarReward = STAR_COST_2

    private val listAllKids = ArrayList<AllKidsModel>()
    private val tempSelectedList = ArrayList<AllKidsModel>()
    private val tempUnselectedList = ArrayList<AllKidsModel>()

    private val kidSelectedProfileAdapter by lazy {
        KidProfileAdapter()
    }
    private val kidProfileAdapter by lazy {
        KidProfileAdapter(listener = onKidClicked)
    }

    private val addDoneIconAdapter by lazy {
        ActionButtonAdapter(
            type = ActionButtonAdapter.ButtonActionType.ADD_ICON,
            listener = onAddIconClicked
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() = AddRewardFragment()

        private const val STAR_COST_2 = 2
        private const val STAR_COST_8 = 8
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_reward,
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

            globalReward.observe(viewLifecycleOwner) {
                it?.let { rewardResponse ->
                    listRewards.addAll(rewardResponse.rewards)
                    recommendedRewardAdapter.setItems(listRewards.filter { reward -> reward.star_cost == STAR_COST_2 }
                        .toArrayList())
                }
            }

            detailKid.observe(viewLifecycleOwner) {
                it?.let { it1 ->
                    setKidDetailData(it1)
                }
            }

            createRewardSuccess.observe(viewLifecycleOwner) {
                popToInitialFragment()
            }

            listKids.observe(viewLifecycleOwner) {
                it?.let { kids ->
                    listAllKids.addAll(kids.map { kid ->
                        kid.apply {
                            uiAction = AllKidsModel.UIAction(
                                isSelected = false,
                                isEnable = checkIsKidEligibleToReward(this)
                            )
                        }
                    }.filter { kid -> kid.id != AppPreference.getTempChildId() })
                }
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()
        setRecommendedRewardData()


        viewModel.getDetailKid(profileId = AppPreference.getTempChildId())
        viewModel.getListAllKids()
        viewModel.getListGlobalReward()

        dataBinding.etCustomReward.afterTextChanged {
            if (it.isNotEmpty()) {
                if (selectedReward.id != -1) {
                    resetGlobalRewardThatSelected()
                }
                dataBinding.btnAdd.apply {
                    isEnabled = true
                    isClickable = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }
            } else {
                dataBinding.btnAdd.apply {
                    isEnabled = false
                    isClickable = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primary_disabled
                        )
                    )
                }
            }
        }

        dataBinding.rvKids.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter =
                ConcatAdapter(kidSelectedProfileAdapter, kidProfileAdapter, addDoneIconAdapter)
        }
    }

    private fun setRecommendedRewardData() {
        dataBinding.apply {
            recommendedRewardAdapter = RecommendedRewardAdapter(
                requireContext(),
                arrayListOf(),
                this@AddRewardFragment
            )
            rvRecommendedRewards.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = recommendedRewardAdapter
            }
        }
    }

    private fun setKidDetailData(kidData: AllKidsModel) {
        dataBinding.apply {
            kidSelectedProfileAdapter.setProfileListInto(arrayListOf(kidData.apply {
                uiAction = AllKidsModel.UIAction(isSelected = true, isEnable = true)
            }))
        }
    }

    private fun popToInitialFragment() {
        val backStackCount: Int = requireActivity().supportFragmentManager.backStackEntryCount
        val backStackLimit = 3

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
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            ivBack.setOnClickListener(onClickCallback)
            cvTwoStars.setOnClickListener(onClickCallback)
            cvEightStars.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }

            dataBinding.cvTwoStars -> {
                dataBinding.apply {
                    cvTwoStars.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorSecondary
                        )
                    )
                    tvTwoStars.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    cvEightStars.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.transparent
                        )
                    )
                    tvEightStars.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }
                selectedStarReward = STAR_COST_2
                recommendedRewardAdapter.setItems(listRewards.filter { it.star_cost == STAR_COST_2 }
                    .toArrayList())
            }

            dataBinding.cvEightStars -> {
                dataBinding.apply {
                    cvTwoStars.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.transparent
                        )
                    )
                    tvTwoStars.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    cvEightStars.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorSecondary
                        )
                    )
                    tvEightStars.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
                selectedStarReward = STAR_COST_8
                recommendedRewardAdapter.setItems(listRewards.filter { it.star_cost == STAR_COST_8 }
                    .toArrayList())
            }
        }
    }

    override fun onItemClicked(data: RewardModel) {
        selectedReward = data
        resetKidThatSelected()
        dataBinding.apply {
            btnAdd.apply {
                isEnabled = true
                isClickable = true
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
            }

            val listIdKids = ArrayList<Int>()

            btnAdd.setOnClickListener {
                // custom rewards
                if (!etCustomReward.text.isNullOrEmpty()) {
                    viewModel.postCreateReward(
                        createRewardRequest = CreateRewardRequest(
                            title = etCustomReward.text.toString(),
                            description = emptyString,
                            star_cost = selectedStarReward,
                            availability = listIdKids.joinToString(separator = ",")
                        )
                    )
                } else {
                    // recommendation rewards
                    viewModel.postAssignRewardForKids(
                        request = RewardAssignKidRequest(
                            rewardId = data.id,
                            kidId = listIdKids
                        )
                    )
                }
            }
        }
    }

    private val onKidClicked = object : KidProfileAdapter.Listener {
        override fun onKidProfileSelected(data: AllKidsModel) {
            listAllKids.find { kid -> kid.id == data.id }.apply {
                this?.uiAction?.isSelected = this?.uiAction?.isSelected != true
            }
            refreshKidProfileAdapter()
        }
    }

    private val onAddIconClicked = object : ActionButtonAdapter.Listener {
        override fun onActionButtonClicked(type: ActionButtonAdapter.ButtonActionType) {
            if (type == ActionButtonAdapter.ButtonActionType.ADD_ICON) {
                showAllKid()
            } else {
                showSelectedKidOnly()
            }
        }
    }

    private fun refreshKidProfileAdapter() {
        kidProfileAdapter.notifyDataSetChanged()
    }

    private fun showAllKid() {
        addDoneIconAdapter.setIconType(ActionButtonAdapter.ButtonActionType.DONE_ICON)
        tempSelectedList.clear()
        tempUnselectedList.clear()

        tempSelectedList.addAll(listAllKids.filter { kid -> kid.uiAction.isSelected })
        tempUnselectedList.addAll(listAllKids.filter { kid -> !kid.uiAction.isSelected })

        val combinedList = tempSelectedList + tempUnselectedList
        kidProfileAdapter.setProfileListInto(combinedList.distinctBy { it.id }.toArrayList())
    }

    private fun showSelectedKidOnly() {
        addDoneIconAdapter.setIconType(ActionButtonAdapter.ButtonActionType.ADD_ICON)
        tempSelectedList.clear()
        tempUnselectedList.clear()

        tempSelectedList.addAll(listAllKids.filter { kid -> kid.uiAction.isSelected })
        kidProfileAdapter.setProfileListInto(tempSelectedList.distinctBy { it.id }.toArrayList())
    }

    private fun resetGlobalRewardThatSelected() {
        selectedReward = RewardModel()
        recommendedRewardAdapter.setItems(listRewards.map {
            it.apply {
                isSelected = false
            }
        }.toArrayList())
    }

    private fun resetKidThatSelected() {
        kidProfileAdapter.setProfileListInto(listAllKids.map {
            it.apply {
                it.uiAction = AllKidsModel.UIAction(
                    isSelected = false,
                    isEnable = checkIsKidEligibleToReward(it)
                )
            }
        }.toArrayList())
    }

    private fun checkIsKidEligibleToReward(kid: AllKidsModel): Boolean {
        if (kid.RewardAvailable.size == 2) {
            return false
        } else if (kid.RewardAvailable.size < 2) {
            var isEligible = true
            kid.RewardAvailable.forEach {
                if (it.globalRewardId == selectedReward.id && selectedReward.id != -1) {
                    isEligible = false
                    return@forEach
                }
            }
            kid.RewardAvailable.forEach {
                if (it.reward?.star_cost == selectedStarReward || it.globalReward?.star_cost == selectedStarReward) {
                    isEligible = false
                }
            }
            return isEligible
        } else {
            return true
        }
    }

}