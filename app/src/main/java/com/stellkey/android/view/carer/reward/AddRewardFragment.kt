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

    companion object {
        @JvmStatic
        fun newInstance() = AddRewardFragment()
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
                    setRecommendedRewardData(rewardResponse.rewards)
                }
            }

            detailKid.observe(viewLifecycleOwner) {
                it?.let { it1 ->
                    setKidDetailData(it1)
                }
            }

            createRewardSuccess.observe(viewLifecycleOwner){
                popToInitialFragment()
            }

        }

        setView()
        setOnClick()
    }

    private fun setView() {
        (activity as HomeAct).showMenu(isShow = false)
        onBackPressed()

        viewModel.getListGlobalReward()
        viewModel.getDetailKid(profileId = AppPreference.getTempChildId())

        dataBinding.apply {

        }
    }

    private fun setRecommendedRewardData(rewardData: ArrayList<RewardModel>) {
        dataBinding.apply {
            recommendedRewardAdapter = RecommendedRewardAdapter(
                requireContext(),
                rewardData,
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
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.ivBack -> {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onItemClicked(data: RewardModel) {
        dataBinding.apply {
            if (data.isSelected)
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
            else
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