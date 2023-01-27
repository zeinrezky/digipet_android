package com.stellkey.android.view.child.reward

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.stellkey.android.R
import com.stellkey.android.constant.PetTheme
import com.stellkey.android.databinding.FragmentChildRewardBinding
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.AssignmentsModel
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.KidRewardRedemption
import com.stellkey.android.model.TaskStarModel
import com.stellkey.android.model.request.KidRedeemRewardRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.home.adapter.KidTodayTaskAdapter
import com.stellkey.android.view.child.home.adapter.MyProgressAdapter
import com.stellkey.android.view.child.pet.ChildPetFragment
import com.stellkey.android.view.child.profile.ChildProfileFragment
import com.stellkey.android.view.child.reward.adapter.ChildRewardAdapter
import com.stellkey.android.view.intro.auth.LoginChooseProfileFragment
import org.koin.android.ext.android.inject

class ChildRewardFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentChildRewardBinding

    private val viewModel by inject<ChildViewModel>()
    private val childRewardAdapter by lazy {
        ChildRewardAdapter(listOf(), listener = onRewardsClickedForRedeemed)
    }
    private val petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())

    companion object {

        @JvmStatic
        fun newInstance() = ChildRewardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_child_reward, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.vm = viewModel
        dataBinding.lifecycleOwner = this

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showWaitingDialog()
                } else {
                    hideWaitingDialog()
                }
            }

            kidInfoResponse.observe(viewLifecycleOwner) {
                it?.let { it1 -> setKidView(it1) }
            }

            kidRewardsAvailableRedemption.observe(viewLifecycleOwner) {
                setRewardsData(it)
            }

            kidRedeemRewardSuccess.observe(viewLifecycleOwner) {
                viewModel.getKidListRewardsAvailableForRedemption()
            }
        }

        setView()
        initAnimation()
        setOnClick()
    }

    private fun setView() {
        setRecyclerViewRewards()

        viewModel.getKidInfo()
        viewModel.getKidListRewardsAvailableForRedemption()
    }


    private fun setKidView(data: KidInfoModel) {
        dataBinding.apply {
            tvChildLevel.textOrNull = data.level.level.toString()
            piChildLevel.progress = 100 - data.level.percentageToNextLevel
            tvTodayTaskStar.text = "${data.level.starsTotal - data.starsSpent}"
            val availableForGem = (data.tasksCompleted ?: 0) - (data.rubiesSpent ?: 0)
            tvTodayTaskDiamond.text = availableForGem.toString()
        }
    }

    private fun setRecyclerViewRewards() {
        dataBinding.rvReward.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = childRewardAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRewardsData(data: List<KidRewardRedemption>) {
        childRewardAdapter.listRewards = data
        childRewardAdapter.notifyDataSetChanged()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun setOnClick() {
        dataBinding.apply {
            cvSwitchUser.setOnClickListener(onClickCallback)
            clPet.setOnClickListener(onClickCallback)
            clKidLevel.setOnClickListener(onClickCallback)
        }

        dataBinding.viewPetAnimation.lottiePet.setOnClickListener {
            dataBinding.viewPetAnimation.lottiePet.apply {
                setAnimation(petThemeColor.gigglePose)
                repeatCount = LottieDrawable.INFINITE
                playAnimation()
            }
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            dataBinding.cvSwitchUser -> {
                (activity as ChildMainAct).showMenu(isShow = false)
                addFragment(LoginChooseProfileFragment.newInstance())
            }

            dataBinding.clPet -> {
                addFragment(ChildPetFragment.newInstance())
            }

            dataBinding.clKidLevel -> {
                addFragment(ChildProfileFragment.newInstance())
            }
        }
    }

    private val onRewardsClickedForRedeemed = object : ChildRewardAdapter.Listener {
        override fun onRewardClickedForRedeemed(data: KidRewardRedemption) {
            viewModel.postKidRewardRedeemption(
                request = KidRedeemRewardRequest(
                    globalRewardId = if (data.isGlobal) data.id else null,
                    rewardId = if (!data.isGlobal) data.id else null
                )
            )
        }
    }

    private fun initAnimation() {
        dataBinding.viewPetAnimation.lottiePet.apply {
            setAnimation(petThemeColor.normalPose)
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    /**
     * Giggle-Pose = happiness x% - x%, hungry x% - x% ||  When Clicked Pet
     * Hungry-Pose = happiness > 50, hungry < 50
     * Normal-Pose = happiness >= 50, hungry >= 50
     * Yummy-Pose = happiness >50, hungry >= 50
     * Angry-Pose = happingess < 50, hungry < 50
     * Happy-Pose = happiness >= 75, hungry >= 75
     **/

    private fun updatePetTheme() {

    }

    private fun updateHappiness(happiness: Int) {
        when (happiness) {
            in 0..25 -> {

            }

            in 26..50 -> {

            }

            in 51..75 -> {

            }

            in 76..100 -> {

            }
        }
    }

    private fun updateEat(hunger: Int) {
        when (hunger) {
            in 0..25 -> {

            }

            in 26..50 -> {

            }

            in 51..75 -> {

            }

            in 76..100 -> {

            }
        }
    }
}