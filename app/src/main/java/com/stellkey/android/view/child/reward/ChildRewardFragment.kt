package com.stellkey.android.view.child.reward

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.stellkey.android.R
import com.stellkey.android.constant.PetEmotion
import com.stellkey.android.constant.PetTheme
import com.stellkey.android.databinding.FragmentChildRewardBinding
import com.stellkey.android.helper.extension.loadFromUrl
import com.stellkey.android.helper.extension.onBackBlockPressed
import com.stellkey.android.helper.extension.textOrNull
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.KidRewardRedemption
import com.stellkey.android.model.request.KidRedeemRewardRequest
import com.stellkey.android.util.AppPreference
import com.stellkey.android.view.base.BaseFragment
import com.stellkey.android.view.child.ChildMainAct
import com.stellkey.android.view.child.ChildViewModel
import com.stellkey.android.view.child.dialog.BasicKidDialog
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
    private var petThemeColor = PetTheme(AppPreference.getKidPetColorTheme())

    private var kidRewardRedemptionSelected = KidRewardRedemption()

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
                showConfirmationRedeemDialog()
            }
        }

        setView()
        initAnimation()
        setOnClick()
    }

    private fun setView() {
        onBackBlockPressed()
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

    private fun setOnClick() {
        dataBinding.apply {
            cvSwitchUser.setOnClickListener(onClickCallback)
            clPet.setOnClickListener(onClickCallback)
            clKidLevel.setOnClickListener(onClickCallback)
        }

        dataBinding.viewPetAnimation.lottiePet.setOnClickListener {
            viewModel.postKidTapThePet()
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
            kidRewardRedemptionSelected = data
            viewModel.postKidRewardRedeemption(
                request = KidRedeemRewardRequest(
                    globalRewardId = if (data.isGlobal) data.id else null,
                    rewardId = if (!data.isGlobal) data.id else null
                )
            )
        }
    }

    fun showConfirmationRedeemDialog(){
        val basicParam = BasicKidDialog.BasicKidDialogParam(
            title = getString(R.string.kid_redeem_dialog_kid_info_title),
            subtitle = getString(
                R.string.kid_redeem_dialog_kid_info_subtitle,
                kidRewardRedemptionSelected.star_cost.toString()
            ),
            isShowingImage = true,
            isImageFromRes = false,
            imageAttachmentDialogLink = kidRewardRedemptionSelected.icon,
            imageTitle = kidRewardRedemptionSelected.title
        )

        BasicKidDialog(onCloseClicked = {
            viewModel.getKidListRewardsAvailableForRedemption()
        }).apply {
            arguments = Bundle().apply {

                putParcelable(BasicKidDialog.BASIC_KID_DIALOG_PARAM, basicParam)
            }
        }.show(childFragmentManager, BasicKidDialog.TAG)
    }

    private fun initAnimation() {
        dataBinding.viewPetAnimation.ivFoodUsing.loadFromUrl(AppPreference.getKidPetFoodAssignment())
        dataBinding.viewPetAnimation.ivPetDecorUsing.loadFromUrl(AppPreference.getKidPetDecorAssignment())
        dataBinding.viewPetAnimation.lottiePet.apply {
            when (AppPreference.getPetCurrentEmotion()) {
                PetEmotion.PET_EMOTION_ANGRY -> {
                    setAnimation(petThemeColor.angryPose)
                }

                PetEmotion.PET_EMOTION_HAPPY -> {
                    setAnimation(petThemeColor.happyPose)
                }

                PetEmotion.PET_EMOTION_HUNGRY -> {
                    setAnimation(petThemeColor.hungryPose)
                }

                PetEmotion.PET_EMOTION_YUMMY -> {
                    setAnimation(petThemeColor.yummyPose)
                }

                PetEmotion.PET_EMOTION_NORMAL -> {
                    setAnimation(petThemeColor.normalPose)
                }
            }
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }
}