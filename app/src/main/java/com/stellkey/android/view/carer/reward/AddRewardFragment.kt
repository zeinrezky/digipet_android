package com.stellkey.android.view.carer.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.stellkey.android.R
import com.stellkey.android.databinding.FragmentAddRewardBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.ImageCornerOptions
import com.stellkey.android.helper.extension.emptyString
import com.stellkey.android.helper.extension.loadImage
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.RewardModel
import com.stellkey.android.model.request.CreateRewardRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.carer.home.HomeAct
import com.stellkey.android.view.carer.profile.ProfileViewModel
import com.stellkey.android.view.carer.profile.adapter.RecommendedRewardAdapter
import org.koin.android.ext.android.inject

class AddRewardFragment : BaseFragment(), RecommendedRewardAdapter.Listener {

    private lateinit var dataBinding: FragmentAddRewardBinding

    //private val binding by viewBinding<FragmentAddRewardBinding>()
    private val viewModel by inject<ProfileViewModel>()

    private lateinit var recommendedRewardAdapter: RecommendedRewardAdapter
    private val listRewards = ArrayList<RewardModel>()
    private var selectedReward = RewardModel()

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
                    recommendedRewardAdapter.setItems(listRewards.filter { it.star_cost == STAR_COST_2 }
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

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()
        setRecommendedRewardData()


        viewModel.getDetailKid(profileId = AppPreference.getTempChildId())
        viewModel.getListGlobalReward()
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
            ivKidAvatar.loadImage(
                kidData.profileIcon.icon,
                ImageCornerOptions.ROUNDED,
                100
            )
            tvKidName.textOrNull = kidData.name
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
                recommendedRewardAdapter.setItems(listRewards.filter { it.star_cost == STAR_COST_8 }
                    .toArrayList())
            }
        }
    }

    override fun onItemClicked(data: RewardModel) {
        selectedReward = data
        dataBinding.apply {
            if (data.isSelected) {
                btnAdd.apply {
                    isEnabled = true
                    isClickable = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    setOnClickListener {
                        viewModel.postCreateReward(
                            createRewardRequest = CreateRewardRequest(
                                title = data.title,
                                description = emptyString,
                                star_cost = data.star_cost,
                                availability = "10"
                            )
                        )
                    }
                }
            } else {
                btnAdd.apply {
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

    }

}