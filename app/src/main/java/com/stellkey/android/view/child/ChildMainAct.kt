package com.stellkey.android.view.child

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.stellkey.android.R
import com.stellkey.android.databinding.ActivityChildMainBinding
import com.stellkey.android.helper.extension.setTransparentStatusBar
import com.stellkey.android.helper.extension.viewBinding
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.Constant
import com.stellkey.android.util.EventBusBinder
import com.stellkey.android.view.base.BaseAct
import com.stellkey.android.view.child.home.ChildHomeFragment
import com.stellkey.android.view.child.profile.ChildProfileFragment
import com.stellkey.android.view.child.reward.ChildRewardFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChildMainAct : BaseAct() {

    private val binding by viewBinding<ActivityChildMainBinding>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(gotoScreen: NavigationChildHomeEvent) {
        when (gotoScreen.goToScreen) {
            Constant.KidMenu.REWARD -> {
                addFragment(ChildRewardFragment.newInstance())
            }

            Constant.KidMenu.HOME -> {
                addFragment(ChildHomeFragment.newInstance())
            }

            Constant.KidMenu.LOG -> {
//                addFragment(ChildHomeFragment.newInstance())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_main)

        EventBusBinder.bind(this)
        setView()
        setOnClick()
    }

    private fun setView() {
        addFragment(ChildHomeFragment.newInstance())

        if (AppPreference.isUpdateLocale()) {
            addFragment(ChildProfileFragment.newInstance())
        } else
            addFragment(ChildHomeFragment.newInstance())

        setTransparentStatusBar()
    }

    fun showMenu(isShow: Boolean) {
        binding.apply {
            clKidBtmMenu.isVisible = isShow
            viewKidDividerBottom.isVisible = isShow
        }
    }

    private fun setOnClick() {
        binding.apply {
            ivReward.setOnClickListener(onClickCallback)
            ivHome.setOnClickListener(onClickCallback)
            ivLog.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.ivReward -> {
                binding.apply {
                    ivReward.setImageResource(R.drawable.ic_reward)
                    ivRewardIndicator.isVisible = true
                    ivHome.setImageResource(R.drawable.ic_kid_home_inactive)
                    ivHomeIndicator.isVisible = false
                    ivLog.setImageResource(R.drawable.ic_kid_log_inactive)
                    ivLogIndicator.isVisible = false
                    EventBus.getDefault().post(NavigationChildHomeEvent(Constant.KidMenu.REWARD))
                }
            }

            binding.ivHome -> {
                binding.apply {
                    ivReward.setImageResource(R.drawable.ic_reward_inactive)
                    ivRewardIndicator.isVisible = false
                    ivHome.setImageResource(R.drawable.ic_kid_home)
                    ivHomeIndicator.isVisible = true
                    ivLog.setImageResource(R.drawable.ic_kid_log_inactive)
                    ivLogIndicator.isVisible = false
                    EventBus.getDefault().post(NavigationChildHomeEvent(Constant.KidMenu.HOME))
                }
            }

            binding.ivLog -> {
                binding.apply {
                    ivReward.setImageResource(R.drawable.ic_reward_inactive)
                    ivRewardIndicator.isVisible = false
                    ivHome.setImageResource(R.drawable.ic_kid_home_inactive)
                    ivHomeIndicator.isVisible = false
                    ivLog.setImageResource(R.drawable.ic_kid_log)
                    ivLogIndicator.isVisible = true
                    EventBus.getDefault().post(NavigationChildHomeEvent(Constant.KidMenu.LOG))
                }
            }
        }
    }

    class NavigationChildHomeEvent(val goToScreen: String)
}